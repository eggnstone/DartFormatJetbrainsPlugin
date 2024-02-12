package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.application.ApplicationManager
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.Version
import dev.eggnstone.plugins.jetbrains.dartformat.tools.JsonTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.OsTools
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.mime.MultipartEntityBuilder
import java.net.SocketTimeoutException

class ExternalDartFormat
{
    companion object
    {
        const val CLASS_NAME = "ExternalDartFormat"
        val instance = ExternalDartFormat()
    }

    private val channel = Channel<FormatJob>()
    private var dartFormatClient: DartFormatClient? = null
    private var mainJob: Job? = null
    private var alreadyNotifiedAboutExternalDartFormatProcessDeath = false

    @OptIn(DelicateCoroutinesApi::class)
    fun init()
    {
        val methodName = "$CLASS_NAME.init"
        Logger.logDebug("$methodName()")

        if (mainJob != null)
            return

        mainJob = GlobalScope.launch { run() }
    }

    private suspend fun run()
    {
        val methodName = "$CLASS_NAME.run"
        Logger.logDebug("$methodName: START")

        var lastFileName: String? = null

        try
        {
            val connection = ApplicationManager.getApplication().messageBus.connect()
            connection.subscribe(AppLifecycleListener.TOPIC, object : AppLifecycleListener
            {
                override fun appClosing()
                {
                    Logger.logDebug("$methodName: appClosing")

                    NotificationTools.notifyInfo(NotificationInfo(
                        content = null,
                        fileName = null,
                        links = null,
                        origin = null,
                        project = null,
                        title = "Shutting down external dart_format ..."
                    ))
                    // TODO: do not shut down when start failed or process dead
                    try
                    {
                        runBlocking {
                            withTimeout(Constants.WAIT_FOR_FORMAT_IN_SECONDS * 1000L) {
                                Logger.logDebug("$methodName: sending quit")
                                channel.send(FormatJob(command = "Quit", inputText = null, config = null, fileName = null))
                                Logger.logDebug("$methodName: sent quit")
                                return@withTimeout "OK"
                            }
                        }

                        NotificationTools.notifyInfo(NotificationInfo(
                            content = null,
                            fileName = null,
                            links = null,
                            origin = null,
                            project = null,
                            title = "Shut down external dart_format."
                        ))
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

                        NotificationTools.notifyError(NotificationInfo(
                            content = null,
                            fileName = null,
                            listOf(reportErrorLink),
                            origin = null,
                            project = null,
                            title = title
                        ))
                    }
                }
            })

            val externalDartFormatFilePath = OsTools.getExternalDartFormatFilePathOrException()
            if (externalDartFormatFilePath !is String)
            {
                val title = "Failed to start external dart_format: " + if (externalDartFormatFilePath is Throwable) externalDartFormatFilePath.message else externalDartFormatFilePath.toString()
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
                NotificationTools.notifyError(NotificationInfo(
                    content = content,
                    fileName = null,
                    listOf(checkInstallationInstructionsLink, reportErrorLink),
                    origin = null,
                    project = null,
                    title = title
                ))
                return
            }

            val processBuilder = ProcessBuilder(externalDartFormatFilePath, "--web", "--errors-as-json", "--log-to-temp-file")

            Logger.logDebug("Starting external dart_format: ${processBuilder.command().joinToString(separator = " ")}")
            NotificationTools.notifyInfo(NotificationInfo(
                content = null,
                fileName = null,
                links = null,
                origin = null,
                project = null,
                title = "Starting external dart_format ...\nThis may take a few seconds."
            ))

            val result: Any = withContext(Dispatchers.IO) { processBuilder.start() }

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
                NotificationTools.notifyError(NotificationInfo(
                    content = content,
                    fileName = null,
                    listOf(checkInstallationInstructionsLink, reportErrorLink),
                    origin = null,
                    project = null,
                    title = title
                ))
                return
            }

            @Suppress("USELESS_CAST")
            val process = result as Process

            if (process.isAlive)
            {
                NotificationTools.notifyInfo(NotificationInfo(
                    content = null,
                    fileName = null,
                    links = null,
                    origin = null,
                    project = null,
                    title = "External dart_format process is alive.\nWaiting for connection details ..."
                ))
            }
            else
                throw Exception("External dart_format process is dead.")

            val inputStreamReader = StreamReader(process.inputStream)
            val errorStreamReader = StreamReader(process.errorStream)
            val readLineResponse = TimedReader.readLine(process, inputStreamReader, errorStreamReader, Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_START_IN_SECONDS, "connection details from external dart_format")
            @Suppress("FoldInitializerAndIfToElvis", "RedundantSuppression")
            if (readLineResponse == null)
                return

            val jsonEncodedResponse = readLineResponse.stdOut ?: readLineResponse.stdErr ?: "<no response>"
            val jsonResponse = JsonTools.parseOrNull(jsonEncodedResponse)
            if (jsonResponse == null)
            {
                val title = "External dart_format: Expected connection details in JSON but received plain text."

                var content = ""
                if (readLineResponse.stdOut != null)
                    content += "\nStdOut: ${readLineResponse.stdOut}"
                content += TimedReader.receiveLines(inputStreamReader, "\nStdOut: ") ?: ""
                if (readLineResponse.stdErr != null)
                    content += "\nStdErr: ${readLineResponse.stdErr}"
                content += TimedReader.receiveLines(errorStreamReader, "\nStdErr: ") ?: ""
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

                NotificationTools.notifyError(NotificationInfo(
                    content = content.ifEmpty { null },
                    fileName = null,
                    links = listOf(checkInstallationInstructionsLink, reportErrorLink),
                    origin = null,
                    project = null,
                    title = title
                ))
                return
            }

            val baseUrl = JsonTools.getString(jsonResponse, "Message", "")
            val currentVersion = Version.parseOrNull(JsonTools.getString(jsonResponse, "CurrentVersion", ""))
            val latestVersion = Version.parseOrNull(JsonTools.getString(jsonResponse, "LatestVersion", ""))
            Logger.logDebug("$methodName: baseUrl:        $baseUrl")
            Logger.logDebug("$methodName: currentVersion: $currentVersion")
            Logger.logDebug("$methodName: latestVersion:  $latestVersion")
            dartFormatClient = DartFormatClient(baseUrl)

            val httpResponse = dartFormatClient!!.get("/status")
            if (httpResponse.statusCode() != 200)
                throw Exception("External dart_format: Requested status but got: ${httpResponse.statusCode()} ${httpResponse.body()}")

            NotificationTools.notifyInfo(NotificationInfo(
                content = null,
                fileName = null,
                links = null,
                origin = null,
                project = null,
                title = "External dart_format is ready."
            ))

            if (currentVersion?.isOlderThan(latestVersion) == true)
            {
                val title = "A new version of the dart_format package is available."
                val content = "<pre>Current version: $currentVersion\nLatest version:  $latestVersion</pre>" +
                    "Just execute this again:<pre>dart pub global activate dart_format</pre>"
                val updateLink = NotificationTools.createCheckInstallationInstructionsLink()
                NotificationTools.notifyInfo(NotificationInfo(
                    content = content,
                    fileName = null,
                    links = listOf(updateLink),
                    origin = null,
                    project = null,
                    title = title
                ))
            }

            while (true)
            {
                val formatJob = channel.receive()
                Logger.logDebug("$methodName: Got new job: ${formatJob.command}")
                lastFileName = formatJob.fileName

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
                        NotificationTools.notifyError(NotificationInfo(
                            content = null,
                            fileName = null,
                            listOf(reportErrorLink),
                            origin = null,
                            project = null,
                            title = title
                        ))
                    }
                }

                if (formatJob.command.toLowerCasePreservingASCIIRules() == "format")
                {
                    Logger.logDebug("Calling format()")
                    formatJob.formatResult = formatViaExternalDartFormat(config = formatJob.config!!, inputText = formatJob.inputText!!)
                    Logger.logDebug("Called format()")
                    Logger.logDebug("Calling formatJob.complete()")
                    formatJob.complete()
                    Logger.logDebug("Called formatJob.complete()")
                    continue
                }

                if (formatJob.command.toLowerCasePreservingASCIIRules() == "quit")
                {
                    Logger.logDebug("Calling quit()")
                    formatJob.formatResult = quitExternalDartFormat()
                    Logger.logDebug("Called quit()")
                    Logger.logDebug("Calling formatJob.complete()")
                    formatJob.complete()
                    Logger.logDebug("Called formatJob.complete()")
                    break
                }

                formatJob.formatResult = FormatResult.error("Unknown command: ${formatJob.command}")
                formatJob.complete()
            }

            Logger.logDebug("$methodName: END")
        }
        catch (e: Exception)
        {
            Logger.logError("$methodName ${e.message}")
            NotificationTools.reportThrowable(
                fileName = lastFileName,
                origin = "$methodName/Exception",
                project = null,
                throwable = e
            )
        }
        catch (e: Error)
        {
            // necessary?
            Logger.logError("ERROR $methodName ${e.message}")
            NotificationTools.reportThrowable(
                fileName = lastFileName,
                origin = "$methodName/Error",
                project = null,
                throwable = e
            )
        }
    }

    suspend fun formatViaChannel(inputText: String, config: String, fileName: String): FormatResult
    {
        val methodName = "$CLASS_NAME.formatViaChannel"
        Logger.logDebug("$methodName()")
        val formatJob = FormatJob(command = "Format", inputText = inputText, config = config, fileName = fileName)

        try
        {
            withTimeout(Constants.WAIT_FOR_FORMAT_IN_SECONDS * 1000L) {
                Logger.logDebug("$methodName: sending")
                channel.send(formatJob)
                Logger.logDebug("$methodName: sent.")
                return@withTimeout "OK"
            }

            withTimeout(Constants.WAIT_FOR_FORMAT_IN_SECONDS * 1000L) {
                Logger.logDebug("$methodName: joining")
                formatJob.join()
                Logger.logDebug("$methodName: joined")
                return@withTimeout "OK"
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
                throw Exception("Failed to shut down external dart_format: ${httpResponse.statusCode()} ${httpResponse.body()}")

            return FormatResult.ok("")
        }
        catch (e: Exception)
        {
            return FormatResult.throwable(methodName, e)
        }
        catch (e: Error)
        {
            // necessary?
            return FormatResult.throwable("ERROR $methodName", e)
        }
    }

    private suspend fun formatViaExternalDartFormat(inputText: String, config: String): FormatResult
    {
        val methodName = "$CLASS_NAME.formatViaExternalDartFormat"

        try
        {
            val multipartEntityBuilder = MultipartEntityBuilder.create()
            multipartEntityBuilder.addTextBody("Config", config)
            multipartEntityBuilder.addTextBody("Text", inputText)
            val entity = multipartEntityBuilder.build()

            val httpResponse: CloseableHttpResponse
            try
            {
                Logger.logDebug("$methodName: POST /format")
                httpResponse = dartFormatClient!!.post("/format", entity)
            }
            catch (e: SocketTimeoutException)
            {
                return FormatResult.error("Failed to format via external dart_format: Timeout")
            }

            val dartFormatExceptionJson = httpResponse.getFirstHeader("X-DartFormat-Exception")
            if (dartFormatExceptionJson != null)
            {
                val dartFormatException = JsonTools.parseDartFormatException(dartFormatExceptionJson.value)
                return FormatResult.throwable(methodName, dartFormatException)
            }

            val formattedText = withContext(Dispatchers.IO) { httpResponse.entity.content.readAllBytes() }.decodeToString()

            return FormatResult.ok(formattedText)
        }
        catch (e: Exception)
        {
            return FormatResult.throwable(methodName, e)
        }
        catch (e: Error)
        {
            // necessary?
            return FormatResult.throwable("ERROR $methodName", e)
        }
    }
}
