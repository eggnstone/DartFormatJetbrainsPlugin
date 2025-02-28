package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.io.awaitExit
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfigGetter
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.ReadLineResponse
import dev.eggnstone.plugins.jetbrains.dartformat.data.Version
import dev.eggnstone.plugins.jetbrains.dartformat.enums.ExternalDartFormatState
import dev.eggnstone.plugins.jetbrains.dartformat.enums.FailType
import dev.eggnstone.plugins.jetbrains.dartformat.process.ProcessTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.*
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import java.net.SocketTimeoutException

class ExternalDartFormat
{
    companion object
    {
        const val CLASS_NAME = "ExternalDartFormat"
        val instance = ExternalDartFormat()
    }

    var currentVersionText = "<unknown version>"
        private set

    var notifyWhenReady = false

    var state: ExternalDartFormatState = ExternalDartFormatState.NOT_STARTED
        private set

    private var alreadyNotifiedAboutExternalDartFormatProcessDeath = false
    private var channel: Channel<FormatJob>? = Channel()
    private var dartFormatClient: DartFormatClient? = null
    private var mainJob: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun init()
    {
        val methodName = "$CLASS_NAME.init"
        if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName()")

        if (mainJob != null)
            return

        mainJob = GlobalScope.launch { run() }
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

        var lastVirtualFile: VirtualFile? = null

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

                    if (dartFormatClient == null || channel == null)
                    {
                        Logger.logDebug("$methodName: Not sending quit because dartFormatClient or channel is null.")
                        state = ExternalDartFormatState.STOPPED
                        return
                    }

                    try
                    {
                        runBlocking {
                            withTimeout(Constants.WAIT_FOR_SEND_JOB_QUIT_COMMAND_IN_SECONDS * 1000L) {
                                Logger.logDebug("$methodName: Sending quit")
                                channel!!.send(FormatJob(command = "Quit", inputText = null, config = null, virtualFile = null, project = null))
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
                    catch (e: TimeoutCancellationException)
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
            var shouldInstall = externalDartFormatInfo.localError != null
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
                if (!tryInstall(shouldUpdate))
                {
                    state = if (shouldUpdate) ExternalDartFormatState.FAILED_TO_UPDATE else ExternalDartFormatState.FAILED_TO_INSTALL
                    return
                }

                state = ExternalDartFormatState.STARTING
                externalDartFormatInfo = ExternalDartFormatTools.getExternalDartFormatInfo()
            }

            if (externalDartFormatInfo.localError != null)
            {
                val title = "Failed to find external dart_format: " + externalDartFormatInfo.localError!!.message
                val content = "Did you install the dart_format package?\n" +
                    "Basically just execute this:<pre>dart pub global activate dart_format</pre>"
                val checkInstallationInstructionsLink = NotificationTools.createCheckInstallationInstructionsLink()
                val reportErrorLink = NotificationTools.createReportErrorLink(
                    content = null,
                    gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                    origin = null,
                    stackTrace = null,
                    title = title
                )

                var showReportErrorLink = true
                if (externalDartFormatInfo.localError!!.message.contains("Cannot find the dart_format package: File does not exist at expected location:"))
                    showReportErrorLink = false

                val links = if (showReportErrorLink) listOf(checkInstallationInstructionsLink, reportErrorLink) else listOf(checkInstallationInstructionsLink)
                NotificationTools.notifyError(
                    NotificationInfo(
                        content = content,
                        links,
                        origin = null,
                        project = null,
                        title = title,
                        virtualFile = null
                    )
                )

                state = ExternalDartFormatState.FAILED_TO_START
                return
            }

            val processBuilder = ProcessTools.createProcessBuilder(externalDartFormatInfo.processBuilderInfo!!)
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
            val result: Any = withContext(Dispatchers.IO) { processBuilder.start() }
            if (Constants.LOG_VERBOSE) Logger.logVerbose("External dart_format: Process started.")

            @Suppress("KotlinConstantConditions")
            if (result !is Process)
            {
                val title = "Failed to start external dart_format: " + if (result is Throwable) result.message else result.toString()
                val content = "Did you install the dart_format package?\n" +
                    "Basically just execute this:<pre>dart pub global activate dart_format</pre>"
                val checkInstallationInstructionsLink = NotificationTools.createCheckInstallationInstructionsLink()
                val reportErrorLink = NotificationTools.createReportErrorLink(
                    content = null,
                    gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                    origin = null,
                    stackTrace = null,
                    title = title
                )
                NotificationTools.notifyError(
                    NotificationInfo(
                        content = content,
                        links = listOf(checkInstallationInstructionsLink, reportErrorLink),
                        origin = null,
                        project = null,
                        title = title,
                        virtualFile = null
                    )
                )
                state = ExternalDartFormatState.FAILED_TO_START
                return
            }

            @Suppress("USELESS_CAST")
            val process = result as Process

            if (process.isAlive)
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

            val inputStreamReader = StreamReader(process.inputStream)
            val errorStreamReader = StreamReader(process.errorStream)
            var readLineResponse: ReadLineResponse?

            var stdErrLines: String? = null
            var stdOutLines: String? = null
            while (true)
            {
                readLineResponse = TimedReader.readLine(process, inputStreamReader, errorStreamReader, Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_START_IN_SECONDS, "connection details from external dart_format")
                if (readLineResponse == null)
                    break

                val stdErrLine = readLineResponse.stdErr
                if (stdErrLine != null)
                {
                    //Logger.logDebug("StdErr: $stdErrLine")
                    if (stdErrLines == null)
                        stdErrLines = stdErrLine
                    else
                        stdErrLines += "\n" + stdErrLine
                }

                val stdOutLine = readLineResponse.stdOut
                if (stdOutLine != null)
                {
                    //Logger.logDebug("StdOut: $stdOutLine")
                    if (stdOutLines == null)
                        stdOutLines = stdErrLine
                    else
                        stdOutLines += "\n" + stdOutLine

                    if (stdOutLine.startsWith("{"))
                        break
                    else
                        Logger.logDebug("Unexpected plain text: $stdOutLine")
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
                state = ExternalDartFormatState.FAILED_TO_START
                return
            }

            val jsonEncodedResponse = readLineResponse.stdOut ?: readLineResponse.stdErr ?: "<no response>"
            val jsonResponse = JsonTools.parseOrNull(jsonEncodedResponse)
            if (jsonResponse == null)
            {
                val title = "External dart_format: Expected connection details in JSON but received plain text: " +
                    StringTools.toDisplayString(jsonEncodedResponse, 200)

                var content = ""
                if (stdOutLines != null)
                    content += "\nStdOut1:\n$stdOutLines"
                content += TimedReader.receiveLines(inputStreamReader, "\nStdOut2:\n") ?: ""
                if (stdErrLines != null)
                    content += "\nStdErr1:\n$stdErrLines"
                content += TimedReader.receiveLines(errorStreamReader, "\nStdErr2:\n") ?: ""
                content = content.trim()

                if (content.isNotEmpty())
                    content += "\n"

                content += "Did you install the dart_format package?\n" +
                    "Basically just execute this:<pre>dart pub global activate dart_format</pre>"

                val checkInstallationInstructionsLink = NotificationTools.createCheckInstallationInstructionsLink()
                val reportErrorLink = NotificationTools.createReportErrorLink(
                    content = content.ifEmpty { null },
                    gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                    origin = null,
                    stackTrace = null,
                    title = title
                )

                NotificationTools.notifyError(
                    NotificationInfo(
                        content = content.ifEmpty { null },
                        links = listOf(checkInstallationInstructionsLink, reportErrorLink),
                        origin = null,
                        project = null,
                        title = title,
                        virtualFile = null
                    )
                )
                state = ExternalDartFormatState.FAILED_TO_START
                return
            }

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

            val baseUrl = JsonTools.getString(jsonResponse, "Message", "")
            currentVersionText = JsonTools.getString(jsonResponse, "CurrentVersion", "")
            val currentVersion = Version.parseOrNull(currentVersionText)
            val latestVersionText = JsonTools.getString(jsonResponse, "LatestVersion", "")
            val latestVersion = Version.parseOrNull(latestVersionText)
            Logger.logDebug("$methodName: baseUrl:        $baseUrl")
            Logger.logDebug("$methodName: currentVersion: $currentVersion")
            Logger.logDebug("$methodName: latestVersion:  $latestVersion")
            dartFormatClient = DartFormatClient(baseUrl)

            val httpResponse = dartFormatClient!!.get("/status")
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
                if (Constants.LOG_VERBOSE) Logger.logVerbose("Saving latest version: $latestVersionText")
                DartFormatConfigGetter.get().latestVersionText = latestVersionText

                val title = "A new version of the dart_format package is available and will be installed during the next start."
                NotificationTools.notifyInfo(
                    NotificationInfo(
                        content = null,
                        links = null,
                        origin = null,
                        project = null,
                        title = title,
                        virtualFile = null
                    )
                )
            }

            while (true)
            {
                val formatJob = channel!!.receive()
                if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Got new job: ${formatJob.command}")
                lastVirtualFile = formatJob.virtualFile

                if (!process.isAlive)
                {
                    // TODO: fix
                    if (!alreadyNotifiedAboutExternalDartFormatProcessDeath)
                    {
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
                }

                if (formatJob.command.toLowerCasePreservingASCIIRules() == "format")
                {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Calling format()")
                    val filePath = NotificationTools.getShortFilePath(formatJob.virtualFile!!, formatJob.project)
                    formatJob.formatResult = formatViaExternalDartFormat(config = formatJob.config!!, inputText = formatJob.inputText!!, filePath = filePath)
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Called format()")
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Calling formatJob.complete()")
                    formatJob.complete()
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Called formatJob.complete()")
                    continue
                }

                if (formatJob.command.toLowerCasePreservingASCIIRules() == "quit")
                {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Calling quit()")
                    formatJob.formatResult = quitExternalDartFormat()
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Called quit()")
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Calling formatJob.complete()")
                    formatJob.complete()
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Called formatJob.complete()")
                    break
                }

                formatJob.formatResult = FormatResult.error("Unknown command: ${formatJob.command}")
                formatJob.complete()
            }

            dartFormatClient = null
            channel!!.close()
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

    private suspend fun tryInstall(shouldUpdate: Boolean): Boolean
    {
        val actionEdLower = if (shouldUpdate) "updated" else "installed"
        val actionErUpper = if (shouldUpdate) "Updater" else "Installer"
        val actionIngUpper = if (shouldUpdate) "Updating" else "Installing"
        val actionLower = if (shouldUpdate) "update" else "install"

        val installExternalDartFormatInfo = ExternalDartFormatTools.getInstallExternalDartFormatInfo()
        if (installExternalDartFormatInfo.localError != null)
        {
            state = if (shouldUpdate) ExternalDartFormatState.FAILED_TO_UPDATE else ExternalDartFormatState.FAILED_TO_INSTALL
            throw DartFormatException.localError("Unexpected error: ${installExternalDartFormatInfo.localError.message}")
        }

        val processBuilder = ProcessTools.createProcessBuilder(installExternalDartFormatInfo.processBuilderInfo!!)

        Logger.logDebug("$actionIngUpper external dart_format: ${processBuilder.command().joinToString(separator = " ")}")
        NotificationTools.notifyInfo(
            NotificationInfo(
                content = null,
                links = null,
                origin = null,
                project = null,
                title = "$actionIngUpper external dart_format ...\nThis may take a few seconds.",
                virtualFile = null
            )
        )

        if (Constants.LOG_VERBOSE) Logger.logVerbose("$actionIngUpper external dart_format: Process starting ...")
        val result: Any = withContext(Dispatchers.IO) { processBuilder.start() }
        if (Constants.LOG_VERBOSE) Logger.logVerbose("$actionIngUpper external dart_format: Process started.")

        @Suppress("KotlinConstantConditions")
        if (result !is Process)
        {
            val title = "Failed to $actionLower external dart_format: " + if (result is Throwable) result.message else result.toString()
            val content = "You can try to $actionLower it manually.\n" +
                "Basically just execute this:<pre>dart pub global activate dart_format</pre>"
            val checkInstallationInstructionsLink = NotificationTools.createCheckInstallationInstructionsLink()
            val reportErrorLink = NotificationTools.createReportErrorLink(
                content = null,
                gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                origin = null,
                stackTrace = null,
                title = title
            )
            NotificationTools.notifyError(
                NotificationInfo(
                    content = content,
                    links = listOf(checkInstallationInstructionsLink, reportErrorLink),
                    origin = null,
                    project = null,
                    title = title,
                    virtualFile = null
                )
            )
            return false
        }

        @Suppress("USELESS_CAST")
        val process = result as Process

        var exitCode = -1
        val processWasAlive = process.isAlive
        if (processWasAlive)
        {
            if (Constants.DEBUG_CONNECTION) NotificationTools.notifyInfo(
                NotificationInfo(
                    content = null,
                    links = null,
                    origin = null,
                    project = null,
                    title = "$actionErUpper for external dart_format process is alive.\nWaiting for process to finish ...",
                    virtualFile = null
                )
            )

            if (Constants.LOG_VERBOSE) Logger.logVerbose("$actionIngUpper external dart_format: Waiting for process to finish ...")
            exitCode = process.awaitExit()
            if (Constants.LOG_VERBOSE) Logger.logVerbose("$actionIngUpper external dart_format: Process finished. Exit code: $exitCode")
        }
        else
        {
            exitCode = try
            {
                process.awaitExit()
            }
            catch (e: Exception)
            {
                -1
            }

            Logger.logDebug("$actionErUpper for external dart_format process is dead. ExitCode: $exitCode")
        }

        val stdOut: String = withContext(Dispatchers.IO) { process.inputStream.readAllBytes().decodeToString() }
        val stdErr: String = withContext(Dispatchers.IO) { process.errorStream.readAllBytes().decodeToString() }
        Logger.logDebug(if (stdOut.isEmpty()) "StdOut: <empty>" else "StdOut:\n${stdOut.trim()}")
        Logger.logDebug(if (stdErr.isEmpty()) "StdErr: <empty>" else "StdErr:\n${stdErr.trim()}")
        val dartExecutable = if (OsTools.instance.isWindows) "dart.bat" else "dart"
        val stdErrContainsDartExecutable = stdErr.contains(dartExecutable)

        if (processWasAlive && exitCode == 0)
        {
            if (Constants.LOG_VERBOSE) Logger.logVerbose("$actionIngUpper external dart_format: Process finished.")
        }
        else if (stdErrContainsDartExecutable && (OsTools.instance.isWindows && exitCode == 1) || (!OsTools.instance.isWindows && exitCode == 127))
        {
            // Windows: 1: 'dart.bat' is not recognized as an internal or external command, operable program or batch file.
            // Linux: 127: /bin/bash: line 1: dart: command not found
            Logger.logDebug("$actionIngUpper external dart_format: Process finished. Exit code: $exitCode = Not found")

            val title = "Failed to $actionLower external dart_format. Dart executable not found. ExitCode: $exitCode"
            val content = "Could not find the Dart executable \"" + dartExecutable + "\".\n" +
                "Please make sure that Dart is installed and callable from the commandline.\n" +
                " \n" + // The space is necessary to force an empty line.
                "If you installed Dart via Flutter then see the instructions below."

            NotificationTools.notifyError(
                NotificationInfo(
                    content = content,
                    links = NotificationTools.createUpdateFlutterPathLinks(),
                    origin = null,
                    project = null,
                    title = title,
                    virtualFile = null
                )
            )

            return false
        }
        else
        {
            Logger.logDebug("$actionIngUpper external dart_format: Process finished. Exit code: $exitCode")

            val processState = if (processWasAlive) "Abnormal exit" else "Dead process"
            val title = "Failed to $actionLower external dart_format. $processState. ExitCode: $exitCode"
            val content = "You can try to $actionLower it manually.\n" +
                "Basically just execute this:<pre>dart pub global activate dart_format</pre>"
            val reportContent =
                (if (stdOut.isEmpty()) "StdOut: <empty>" else "StdOut:\n${stdOut.trim()}") + "\n\n" +
                    (if (stdErr.isEmpty()) "StdErr: <empty>" else "StdErr:\n${stdErr.trim()}")
            val checkInstallationInstructionsLink = NotificationTools.createCheckInstallationInstructionsLink()
            val reportErrorLink = NotificationTools.createReportErrorLink(
                content = reportContent,
                gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                origin = null,
                stackTrace = null,
                title = title
            )

            NotificationTools.notifyError(
                NotificationInfo(
                    content = content,
                    links = listOf(checkInstallationInstructionsLink, reportErrorLink),
                    origin = null,
                    project = null,
                    title = title,
                    virtualFile = null
                )
            )

            return false
        }

        NotificationTools.notifyInfo(
            NotificationInfo(
                content = null,
                links = null,
                origin = null,
                project = null,
                title = "Successfully $actionEdLower external dart_format.",
                virtualFile = null
            )
        )

        return true
    }

    fun formatViaChannel(inputText: String, config: String, virtualFile: VirtualFile, project: Project): FormatResult
    {
        val methodName = "$CLASS_NAME.formatViaChannel"
        if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName()")
        val formatJob = FormatJob(command = "Format", inputText = inputText, config = config, virtualFile = virtualFile, project = project)

        try
        {
            runBlocking {
                withTimeout(Constants.WAIT_FOR_SEND_JOB_FORMAT_COMMAND_IN_SECONDS * 1000L) {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: sending")
                    channel!!.send(formatJob)
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: sent.")
                    return@withTimeout "OK"
                }
            }

            runBlocking {
                withTimeout(Constants.WAIT_FOR_JOIN_JOB_FORMAT_COMMAND_IN_SECONDS * 1000L) {
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: joining")
                    formatJob.join()
                    if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: joined")
                    return@withTimeout "OK"
                }
            }
        }
        catch (e: TimeoutCancellationException)
        {
            formatJob.cancel()
            val errorText = "Timeout while waiting for external dart_format."
            Logger.logError("$methodName: $errorText")
            return FormatResult.error(errorText)
        }

        return formatJob.formatResult ?: FormatResult.error("No result")
    }

    private suspend fun quitExternalDartFormat(): FormatResult
    {
        val methodName = "$CLASS_NAME.quitExternalDartFormat"

        try
        {
            val httpResponse = dartFormatClient!!.get("/quit")
            if (httpResponse.statusCode() != 200)
                throw DartFormatException.localError("Failed to shut down external dart_format: ${httpResponse.statusCode()} ${httpResponse.body()}")

            return FormatResult.ok("")
        }
        catch (e: Exception)
        {
            return FormatResult.throwableError(methodName, e)
        }
        catch (e: Error)
        {
            // necessary?
            return FormatResult.throwableError(methodName, e)
        }
    }

    private suspend fun formatViaExternalDartFormat(inputText: String, config: String, filePath: String): FormatResult
    {
        val methodName = "$CLASS_NAME.formatViaExternalDartFormat"

        try
        {
            val multipartEntityBuilder = MultipartEntityBuilder.create()
            multipartEntityBuilder.addTextBody("Config", config)
            // If content type is not set, charset=ISO-8859-1 may be used and special chars like "€" are not transmitted correctly.
            val contentType = ContentType.create("text/plain", Charsets.UTF_8)
            multipartEntityBuilder.addTextBody("Text", inputText, contentType)
            val entity = multipartEntityBuilder.build()

            val httpResponse: CloseableHttpResponse
            try
            {
                Logger.logDebug("$methodName: Calling POST /format ($filePath)")
                httpResponse = dartFormatClient!!.post("/format", entity)
                if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName: Called POST /format")
            }
            catch (e: SocketTimeoutException)
            {
                Logger.logDebug("$methodName: While calling POST /format: $e")
                return FormatResult.error("Failed to format via external dart_format: Timeout")
            }

            @Suppress("UastIncorrectHttpHeaderInspection")
            val dartFormatExceptionJson = httpResponse.getFirstHeader("X-DartFormat-Exception")
            if (dartFormatExceptionJson != null)
            {
                val dartFormatException = JsonTools.parseDartFormatException(dartFormatExceptionJson.value)
                return if (dartFormatException.type == FailType.Warning)
                    FormatResult.throwableWarning(methodName, dartFormatException)
                else
                    FormatResult.throwableError(methodName, dartFormatException)
            }

            val result: Any = withContext(Dispatchers.IO) { httpResponse.entity.content.readAllBytes() }.decodeToString()
            @Suppress("KotlinConstantConditions")
            if (result !is String)
                throw DartFormatException.localError("Expected String but got: ${result::class.java.typeName} $result")

            return FormatResult.ok(result)
        }
        catch (e: Exception)
        {
            return FormatResult.throwableError(methodName, e)
        }
        catch (e: Error)
        {
            // necessary?
            return FormatResult.throwableError(methodName, e)
        }
    }
}
