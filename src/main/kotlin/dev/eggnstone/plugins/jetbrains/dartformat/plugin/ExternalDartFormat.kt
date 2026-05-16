package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfigGetter
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.ProcessExitInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.ReadLineResponse
import dev.eggnstone.plugins.jetbrains.dartformat.data.Version
import kotlinx.serialization.json.JsonElement
import dev.eggnstone.plugins.jetbrains.dartformat.enums.ExternalDartFormatState
import dev.eggnstone.plugins.jetbrains.dartformat.process.ProcessInfoOrLocalError
import dev.eggnstone.plugins.jetbrains.dartformat.process.ProcessTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.*
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.time.Duration.Companion.seconds

// Registered automatically via @Service; no plugin.xml entry required. The platform injects
// a CoroutineScope that is cancelled when the plugin unloads, so the dart_format coroutine
// stops cleanly on update/disable instead of leaking past the plugin's lifetime (which was
// the previous GlobalScope.launch behavior).
@Service(Service.Level.APP)
class ExternalDartFormat(private val coroutineScope: CoroutineScope)
{
    companion object
    {
        const val CLASS_NAME = "ExternalDartFormat"

        fun getInstance(): ExternalDartFormat = ApplicationManager.getApplication().service()
    }

    @Volatile
    var currentVersionText = "(unknown version)"
        private set

    // Captured from the dart_format 2.2.0+ startup JSON. Null when dart_format hasn't started yet
    // or is an older binary that only emits `Message`. Read from the EDT (settings diagnostics,
    // report-error link), set from the background coroutine in startAndConnect.
    @Volatile
    var dartFormatLogFilePath: String? = null
        private set

    @Volatile
    var dartFormatLogFileName: String? = null
        private set

    @Volatile
    var dartFormatProcessId: Int? = null
        private set

    @Volatile
    var notifyWhenReady = false

    // Mutated from the background coroutine (run()), read from the EDT (actions checking state
    // before calling formatViaChannel) and from the appClosing listener. @Volatile gives the
    // cross-thread visibility we need without a full Mutex.
    @Volatile
    var state: ExternalDartFormatState = ExternalDartFormatState.NOT_STARTED
        private set

    @Volatile
    private var alreadyNotifiedAboutExternalDartFormatProcessDeath = false

    @Volatile
    private var channel: Channel<FormatJob>? = Channel()

    @Volatile
    private var dartFormatClient: DartFormatClient? = null

    @Volatile
    private var rpc: DartFormatRpc? = null

    @Volatile
    private var lastVirtualFile: VirtualFile? = null

    init
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("$CLASS_NAME: launching run() on service scope")
        coroutineScope.launch { run() }
    }

    private suspend fun run()
    {
        val methodName = "$CLASS_NAME.run"
        if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: START")

        if (state != ExternalDartFormatState.NOT_STARTED)
        {
            Logger.logError("$methodName: Already tried starting.")
            return
        }

        state = ExternalDartFormatState.STARTING

        lastVirtualFile = null

        try
        {
            val connection = ApplicationManager.getApplication().messageBus.connect()
            connection.subscribe(AppLifecycleListener.TOPIC, object : AppLifecycleListener
            {
                override fun appClosing()
                {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName/appClosing")
                    state = ExternalDartFormatState.STOPPING

                    if (Constants.DEBUG_CONNECTION) NotificationTools.notifyInfo(
                        NotificationInfo(
                            content = null,
                            links = null,
                            origin = null,
                            project = null,
                            title = "Shutting down external dart_format ...",
                            virtualFile = null
                        )
                    )

                    // Snapshot to a local so a parallel null-out (e.g. run() finishing) can't
                    // turn this into an NPE between the check and the send.
                    val quitChannel = channel
                    if (dartFormatClient == null || quitChannel == null)
                    {
                        Logger.logDebug("$methodName: Not sending quit because dartFormatClient or channel is null.")
                        state = ExternalDartFormatState.STOPPED
                        return
                    }

                    try
                    {
                        runBlocking {
                            withTimeout(Constants.WAIT_FOR_SEND_JOB_QUIT_COMMAND_IN_SECONDS.seconds) {
                                Logger.logDebug("$methodName: Sending quit")
                                quitChannel.send(FormatJob(command = "Quit", inputText = null, config = null, virtualFile = null, project = null))
                                if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: sent quit")
                                return@withTimeout "OK"
                            }
                        }

                        if (Constants.DEBUG_CONNECTION) NotificationTools.notifyInfo(
                            NotificationInfo(
                                content = null,
                                links = null,
                                origin = null,
                                project = null,
                                title = "Shut down external dart_format.",
                                virtualFile = null
                            )
                        )
                    }
                    catch (_: TimeoutCancellationException)
                    {
                        val title = "Timeout while waiting for external dart_format to shut down."
                        val reportErrorLink = NotificationTools.createReportErrorLink(
                            content = null,
                            gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                            origin = null,
                            stackTrace = null,
                            title = title
                        )

                        NotificationTools.notifyError(
                            NotificationInfo(
                                content = null,
                                links = listOf(reportErrorLink),
                                origin = null,
                                project = null,
                                title = title,
                                virtualFile = null
                            )
                        )
                    }

                    state = ExternalDartFormatState.STOPPED
                }
            })

            var externalDartFormatInfo = ExternalDartFormatTools.getExternalDartFormatInfo()

            var shouldUpdate = false
            var shouldInstall = externalDartFormatInfo is ProcessInfoOrLocalError.LocalError
            if (shouldInstall)
            {
                if (Constants.LOG_VERBOSE) Logger.logVerbose("Will try to install because no installation found.")
            }
            else
            {
                if (Constants.LOG_VERBOSE) Logger.logVerbose(
                    "Checking versions from the last time we ran:" +
                        " Current version: ${DartFormatConfigGetter.get().currentVersionText}" +
                        " Latest version: ${DartFormatConfigGetter.get().latestVersionText}"
                )
                val currentVersion = Version.parseOrNull(DartFormatConfigGetter.get().currentVersionText)
                val latestVersion = Version.parseOrNull(DartFormatConfigGetter.get().latestVersionText)
                if (currentVersion != null && latestVersion != null && currentVersion.isOlderThan(latestVersion))
                {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("Will try to update because of outdated version.")
                    shouldInstall = true
                    shouldUpdate = true
                }
                else
                {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("Not updating now because version was up-to-date the last time we checked.")
                }
            }

            if (shouldInstall)
            {
                state = if (shouldUpdate) ExternalDartFormatState.UPDATING else ExternalDartFormatState.INSTALLING
                if (!DartFormatInstaller.tryInstall(shouldUpdate) { state = it })
                {
                    state = if (shouldUpdate) ExternalDartFormatState.FAILED_TO_UPDATE else ExternalDartFormatState.FAILED_TO_INSTALL
                    return
                }

                state = ExternalDartFormatState.STARTING
                externalDartFormatInfo = ExternalDartFormatTools.getExternalDartFormatInfo()
            }

            val initialInfo = externalDartFormatInfo
            if (initialInfo is ProcessInfoOrLocalError.LocalError)
            {
                ExternalDartFormatNotifications.notifyFailedToFind(initialInfo.error)
                state = ExternalDartFormatState.FAILED_TO_START
                return
            }

            val startResult = startAndConnect(externalDartFormatInfo as ProcessInfoOrLocalError.Normal)
            if (startResult == null)
            {
                state = ExternalDartFormatState.FAILED_TO_START
                return
            }
            val (process, jsonResponse) = startResult

            if (Constants.DEBUG_CONNECTION) NotificationTools.notifyInfo(
                NotificationInfo(
                    content = null,
                    links = null,
                    origin = null,
                    project = null,
                    title = "Got connection details: $jsonResponse",
                    virtualFile = null
                )
            )

            // Prefer the structured Protocol/Address/Port introduced in dart_format 2.2.0; fall back
            // to the legacy `Message` field for older binaries that still ship a pre-built URL.
            val protocol = JsonTools.getStringOrNull(jsonResponse, "Protocol")
            val address = JsonTools.getStringOrNull(jsonResponse, "Address")
            val port = JsonTools.getIntOrNull(jsonResponse, "Port")
            val baseUrl = if (protocol != null && address != null && port != null)
                "$protocol://$address:$port"
            else
                JsonTools.getString(jsonResponse, "Message", "")

            dartFormatLogFilePath = JsonTools.getStringOrNull(jsonResponse, "LogFilePath")
            dartFormatLogFileName = JsonTools.getStringOrNull(jsonResponse, "LogFileName")
            dartFormatProcessId = JsonTools.getIntOrNull(jsonResponse, "ProcessId")

            currentVersionText = JsonTools.getString(jsonResponse, "CurrentVersion", "")
            val currentVersion = Version.parseOrNull(currentVersionText)
            val latestVersionText = JsonTools.getString(jsonResponse, "LatestVersion", "")
            val latestVersion = Version.parseOrNull(latestVersionText)
            Logger.logDebug("$methodName: baseUrl:        $baseUrl")
            Logger.logDebug("$methodName: currentVersion: $currentVersion")
            Logger.logDebug("$methodName: latestVersion:  $latestVersion")
            Logger.logDebug("$methodName: dartFormatLog:  ${dartFormatLogFilePath}/${dartFormatLogFileName} (pid=${dartFormatProcessId})")
            val client = DartFormatClient(baseUrl)
            dartFormatClient = client
            rpc = DartFormatRpc(client)

            val httpResponse = client.get("/status")
            if (httpResponse.statusCode() != 200)
            {
                state = ExternalDartFormatState.FAILED_TO_START
                throw DartFormatException.localError("External dart_format: Requested status but got: ${httpResponse.statusCode()} ${httpResponse.body()}")
            }

            state = ExternalDartFormatState.STARTED
            if (notifyWhenReady)
            {
                var titleReady = "External dart_format is ready now."
                if (Constants.DEBUG_CONNECTION)
                    titleReady += " $jsonResponse"
                NotificationTools.notifyInfo(
                    NotificationInfo(
                        content = null,
                        links = null,
                        origin = null,
                        project = null,
                        title = titleReady,
                        virtualFile = null
                    )
                )
            }

            if (Constants.LOG_VERBOSE) Logger.logVerbose("Saving current version: $currentVersionText")
            DartFormatConfigGetter.get().currentVersionText = currentVersionText

            if (currentVersion?.isOlderThan(latestVersion) == true)
            {
                // Reached only when auto-update was tried and tryInstall failed. The "Updating ..."
                // and tryInstall-failure notifications already told the user. We still save the
                // latest version so the next IDE start retries automatically (see init at line ~169).
                if (Constants.LOG_VERBOSE) Logger.logVerbose("Saving latest version: $latestVersionText")
                DartFormatConfigGetter.get().latestVersionText = latestVersionText
            }

            runFormatJobLoop(process)

            dartFormatClient = null
            rpc = null
            channel?.close()
            channel = null

            if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: END")
        }
        catch (e: Exception)
        {
            Logger.logError("$methodName: Exception: $e")
            NotificationTools.reportThrowable(
                origin = "$methodName/Exception",
                project = null,
                throwable = e,
                virtualFile = lastVirtualFile
            )
        }
        catch (e: Error)
        {
            // necessary?
            Logger.logError("$methodName: Error: $e")
            NotificationTools.reportThrowable(
                origin = "$methodName/Error",
                project = null,
                throwable = e,
                virtualFile = lastVirtualFile
            )
        }
    }

    private suspend fun runFormatJobLoop(process: Process)
    {
        val methodName = "$CLASS_NAME.runFormatJobLoop"
        // Hoist to locals: channel and rpc are populated by run() before this loop is called and
        // nulled out only after it returns, so we can take a stable reference for the whole loop.
        val activeChannel = requireNotNull(channel) { "$methodName called with null channel" }
        val activeRpc = requireNotNull(rpc) { "$methodName called with null rpc" }

        while (true)
        {
            val formatJob = activeChannel.receive()
            if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Got new job: ${formatJob.command}")
            lastVirtualFile = formatJob.virtualFile

            if (!process.isAlive && !alreadyNotifiedAboutExternalDartFormatProcessDeath)
            {
                // TODO: fix
                alreadyNotifiedAboutExternalDartFormatProcessDeath = true
                val title = "External dart_format process died."
                val reportErrorLink = NotificationTools.createReportErrorLink(
                    content = null,
                    gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                    origin = null,
                    stackTrace = null,
                    title = title
                )
                NotificationTools.notifyError(
                    NotificationInfo(
                        content = null,
                        links = listOf(reportErrorLink),
                        origin = null,
                        project = null,
                        title = title,
                        virtualFile = null
                    )
                )
            }

            when (formatJob.command.toLowerCasePreservingASCIIRules())
            {
                "format" ->
                {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Calling format()")
                    val filePath = NotificationTools.getShortFilePath(formatJob.virtualFile!!, formatJob.project)
                    formatJob.formatResult = activeRpc.format(inputText = formatJob.inputText!!, config = formatJob.config!!, filePath = filePath)
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Called format()")
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Calling formatJob.complete()")
                    formatJob.complete()
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Called formatJob.complete()")
                }
                "quit" ->
                {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Calling quit()")
                    formatJob.formatResult = activeRpc.quit()
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Called quit()")
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Calling formatJob.complete()")
                    formatJob.complete()
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Called formatJob.complete()")
                    return
                }
                else ->
                {
                    formatJob.formatResult = FormatResult.error("Unknown command: ${formatJob.command}")
                    formatJob.complete()
                }
            }
        }
    }

    // Starts the dart_format process and reads its connection-details JSON. Handles two
    // single-shot recovery paths inside its retry loop:
    //   - kernel-binary / SDK-hash mismatch -> re-activate dart_format and retry
    //   - newer dart_format version on pub.dev -> install it and retry
    // Both guarded by local flags so a misconfigured environment cannot loop indefinitely.
    // Returns (process, jsonResponse) on success or null on failure (caller sets FAILED_TO_START).
    private suspend fun startAndConnect(initialInfo: ProcessInfoOrLocalError.Normal): Pair<Process, JsonElement>?
    {
        val methodName = "$CLASS_NAME.startAndConnect"

        var externalDartFormatInfo: ProcessInfoOrLocalError = initialInfo
        var autoRecoveryAttempted = false
        var autoUpdateAttempted = false
        var processOrNull: Process? = null
        var jsonResponseOrNull: JsonElement? = null

        retry@ while (true)
        {
            val normalInfo = externalDartFormatInfo as ProcessInfoOrLocalError.Normal
            val processBuilder = ProcessTools.createProcessBuilder(normalInfo.processBuilderInfo)
            Logger.logDebug("Starting external dart_format: ${processBuilder.command().joinToString(separator = " ")}")
            if (Constants.DEBUG_CONNECTION) NotificationTools.notifyInfo(
                NotificationInfo(
                    content = null,
                    links = null,
                    origin = null,
                    project = null,
                    title = "Starting external dart_format ...\nThis may take a few seconds.",
                    virtualFile = null
                )
            )

            if (Constants.LOG_VERBOSE) Logger.logVerbose("External dart_format: Process starting ...")
            val attemptProcess: Process = withContext(Dispatchers.IO) { processBuilder.start() }
            if (Constants.LOG_VERBOSE) Logger.logVerbose("External dart_format: Process started.")

            if (attemptProcess.isAlive)
            {
                if (Constants.DEBUG_CONNECTION) NotificationTools.notifyInfo(
                    NotificationInfo(
                        content = null,
                        links = null,
                        origin = null,
                        project = null,
                        title = "External dart_format process is alive.\nWaiting for connection details ...",
                        virtualFile = null
                    )
                )
            }
            else
            {
                state = ExternalDartFormatState.FAILED_TO_START
                throw DartFormatException.localError("External dart_format process is dead.")
            }

            val inputStreamReader = StreamReader(attemptProcess.inputStream)
            val errorStreamReader = StreamReader(attemptProcess.errorStream)
            var readLineResponse: ReadLineResponse? = null
            var capturedExitInfo: ProcessExitInfo? = null

            var stdErrLines: String? = null
            var stdOutLines: String? = null
            var sdkDownloadAnnounced = false

            if (Constants.DEBUG_FAKE_KERNEL_MISMATCH)
            {
                Logger.logWarning("$methodName: DEBUG_FAKE_KERNEL_MISMATCH active; killing dart_format and injecting fake stderr.")
                attemptProcess.destroyForcibly()
                attemptProcess.waitFor()
                capturedExitInfo = ProcessExitInfo(
                    stdOutTail = "",
                    stdErrTail = "Can't load Kernel binary: Invalid kernel binary format version.",
                    exitCode = 254
                )
                // leaving readLineResponse = null intentionally so the kernel-mismatch branch fires
            }
            else
            {
                if (Constants.DEBUG_FAKE_FLUTTER_SDK_DOWNLOAD)
                {
                    Logger.logWarning("$methodName: DEBUG_FAKE_FLUTTER_SDK_DOWNLOAD active; injecting fake bootstrap stderr.")
                    for (fakeLine in listOf("Checking Dart SDK version...", "Downloading Dart SDK from Flutter engine 1234567890abcdef..."))
                    {
                        stdErrLines = if (stdErrLines == null) fakeLine else "$stdErrLines\n$fakeLine"
                        if (!sdkDownloadAnnounced && ExternalDartFormatNotifications.isFlutterSdkBootstrapStderr(fakeLine))
                        {
                            sdkDownloadAnnounced = true
                            ExternalDartFormatNotifications.notifyFlutterSdkBootstrapInProgress()
                        }
                    }
                }

                while (true)
                {
                    readLineResponse = TimedReader.readLine(
                        attemptProcess,
                        inputStreamReader,
                        errorStreamReader,
                        Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_START_IN_SECONDS,
                        "connection details from external dart_format",
                        onExit = { exitInfo -> capturedExitInfo = exitInfo }
                    )
                    if (readLineResponse == null)
                        break

                    val stdErrLine = readLineResponse.stdErr
                    if (stdErrLine != null)
                    {
                        if (stdErrLines == null)
                            stdErrLines = stdErrLine
                        else
                            stdErrLines += "\n" + stdErrLine

                        // Tell the user why startup is hanging while Flutter pulls down a fresh
                        // Dart SDK. Once per startAndConnect attempt; the success path will fire
                        // its own "ready" notification when JSON eventually lands.
                        if (!sdkDownloadAnnounced && ExternalDartFormatNotifications.isFlutterSdkBootstrapStderr(stdErrLine))
                        {
                            sdkDownloadAnnounced = true
                            ExternalDartFormatNotifications.notifyFlutterSdkBootstrapInProgress()
                        }
                    }

                    val stdOutLine = readLineResponse.stdOut
                    if (stdOutLine != null)
                    {
                        if (stdOutLines == null)
                            stdOutLines = stdOutLine
                        else
                            stdOutLines += "\n" + stdOutLine

                        if (stdOutLine.startsWith("{"))
                            break
                        else
                            Logger.logDebug("Unexpected plain text: $stdOutLine")
                    }
                }
            }

            if (stdErrLines != null || stdOutLines != null)
            {
                Logger.logDebug("####################")

                if (stdErrLines != null)
                {
                    Logger.logDebug("StdErr:\n$stdErrLines")
                    Logger.logDebug("####################")
                }

                if (stdOutLines != null)
                {
                    Logger.logDebug("StdOut:\n$stdOutLines")
                    Logger.logDebug("####################")
                }
            }

            @Suppress("FoldInitializerAndIfToElvis", "RedundantSuppression")
            if (readLineResponse == null)
            {
                val mergedExitInfo = ProcessExitInfo.combine(stdOutLines, stdErrLines, capturedExitInfo)
                val combinedStdErr = mergedExitInfo.stdErrTail.lowercase()
                val isKernelMismatch =
                    combinedStdErr.contains("invalid kernel binary format version") ||
                    combinedStdErr.contains("invalid sdk hash")

                if (isKernelMismatch && !autoRecoveryAttempted)
                {
                    autoRecoveryAttempted = true
                    Logger.logWarning("$methodName: Stale dart_format snapshot detected (Dart SDK changed). Re-activating once.")
                    NotificationTools.notifyInfo(
                        NotificationInfo(
                            content = null,
                            links = null,
                            origin = null,
                            project = null,
                            title = "Re-activating dart_format because the Dart SDK changed since the last activation ...",
                            virtualFile = null
                        )
                    )

                    if (DartFormatInstaller.tryInstall(shouldUpdate = false) { state = it })
                    {
                        externalDartFormatInfo = ExternalDartFormatTools.getExternalDartFormatInfo()
                        if (externalDartFormatInfo is ProcessInfoOrLocalError.Normal)
                            continue@retry
                    }
                }

                TimedReader.notifyUnexpectedProcessExit("connection details from external dart_format", mergedExitInfo)
                return null
            }

            val jsonEncodedResponse = readLineResponse.stdOut ?: readLineResponse.stdErr ?: "(no response)"
            val parsedJson = JsonTools.parseOrNull(jsonEncodedResponse)
            if (parsedJson == null)
            {
                ExternalDartFormatNotifications.notifyExpectedJsonButReceivedPlainText(jsonEncodedResponse, stdOutLines, stdErrLines, inputStreamReader, errorStreamReader)
                return null
            }

            val advertisedCurrentText = JsonTools.getString(parsedJson, "CurrentVersion", "")
            val advertisedLatestText = JsonTools.getString(parsedJson, "LatestVersion", "")
            val advertisedCurrent = Version.parseOrNull(advertisedCurrentText)
            val advertisedLatest = Version.parseOrNull(advertisedLatestText)
            val newerVersionDetected = Constants.DEBUG_FAKE_NEW_VERSION ||
                (advertisedCurrent?.isOlderThan(advertisedLatest) == true)

            if (!autoUpdateAttempted && newerVersionDetected)
            {
                autoUpdateAttempted = true
                val realUpdate = advertisedCurrent?.isOlderThan(advertisedLatest) == true
                val targetVersionLabel = if (realUpdate) advertisedLatestText else "[simulated]"
                Logger.logWarning("$methodName: Newer dart_format version available ($targetVersionLabel). Updating now.")
                NotificationTools.notifyInfo(
                    NotificationInfo(
                        content = null,
                        links = null,
                        origin = null,
                        project = null,
                        title = "A new version of dart_format is available. Updating to $targetVersionLabel ...",
                        virtualFile = null
                    )
                )
                attemptProcess.destroyForcibly()
                attemptProcess.waitFor()
                state = ExternalDartFormatState.UPDATING
                val installOk = DartFormatInstaller.tryInstall(shouldUpdate = true) { state = it }
                if (installOk)
                    externalDartFormatInfo = ExternalDartFormatTools.getExternalDartFormatInfo()

                // Always restart: we killed the process. If install failed, the next iteration starts
                // the old version again, but autoUpdateAttempted is now true so the update branch is
                // skipped and the legacy "will be installed during the next start" path takes over.
                state = ExternalDartFormatState.STARTING
                continue@retry
            }

            processOrNull = attemptProcess
            jsonResponseOrNull = parsedJson
            break@retry
        }

        return Pair(processOrNull, jsonResponseOrNull)
    }


    fun formatViaChannel(inputText: String, config: String, virtualFile: VirtualFile, project: Project): FormatResult
    {
        val methodName = "$CLASS_NAME.formatViaChannel"
        if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName()")
        val formatJob = FormatJob(command = "Format", inputText = inputText, config = config, virtualFile = virtualFile, project = project)

        // Snapshot channel to a local so the BG coroutine can null it out (during shutdown)
        // without turning this into an NPE between the read and the send.
        val sendChannel = channel ?: return FormatResult.error("Cannot format: external dart_format channel is not available.")

        try
        {
            runBlocking {
                withTimeout(Constants.WAIT_FOR_SEND_JOB_FORMAT_COMMAND_IN_SECONDS.seconds) {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: sending")
                    sendChannel.send(formatJob)
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: sent.")
                    return@withTimeout "OK"
                }
            }

            runBlocking {
                withTimeout(Constants.WAIT_FOR_JOIN_JOB_FORMAT_COMMAND_IN_SECONDS.seconds) {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: joining")
                    formatJob.join()
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: joined")
                    return@withTimeout "OK"
                }
            }
        }
        catch (_: TimeoutCancellationException)
        {
            formatJob.cancel()
            val errorText = "Timeout while waiting for external dart_format."
            Logger.logError("$methodName: $errorText")
            return FormatResult.error(errorText)
        }

        return formatJob.formatResult ?: FormatResult.error("No result")
    }

}
