package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
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
    private var dartFormatClient : DartFormatClient? = null
    private var mainJob: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun init(project: Project)
    {
        val methodName = "$CLASS_NAME.init"
        Logger.log("$methodName: $project")

        if (mainJob != null)
            return

        mainJob = GlobalScope.launch { run() }
    }

    private suspend fun run()
    {
        val methodName = "$CLASS_NAME.run"
        Logger.log("$methodName: START")

        try
        {
            val connection = ApplicationManager.getApplication().messageBus.connect()
            connection.subscribe(AppLifecycleListener.TOPIC, object : AppLifecycleListener
            {
                override fun appClosing()
                {
                    Logger.log("$methodName: appClosing")

                    NotificationTools.notifyInfo(listOf("Shutting down external dart_format ..."), ProjectManager.getInstance().defaultProject)
                    runBlocking {
                        withTimeout(Constants.WAIT_FOR_FORMAT_IN_SECONDS * 1000L) {
                            Logger.log("$methodName: sending quit")
                            channel.send(FormatJob(command = "Quit", inputText = null, config = null))
                            Logger.log("$methodName: sent quit")
                            return@withTimeout "OK"
                        }
                    }
                    NotificationTools.notifyInfo(listOf("Shut down external dart_format."), ProjectManager.getInstance().defaultProject)
                }
            })

            val processBuilder: ProcessBuilder = if (OsTools.isWindows())
                ProcessBuilder("cmd", "/c", "dart_format", "--web", "--errors-as-json", "--log-to-temp-file")
            else
                ProcessBuilder("dart_format", "--web", "--errors-as-json", "--log-to-temp-file")

            val process = withContext(Dispatchers.IO) {
                processBuilder.start()
            }

            if (!process.isAlive)
                throw Exception("Failed to start external dart_format: process is dead.")

            Logger.log("$methodName: External dart_format started.")

            val inputReader = StreamReader(process.inputStream)
            val jsonEncodedResponse = TimedReader.readLine(process, inputReader, Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_START_IN_SECONDS)
            val jsonResponse = JsonTools.parse(jsonEncodedResponse)
            val baseUrl = JsonTools.getString(jsonResponse, "Message", "")
            Logger.log("$methodName: baseUrl: $baseUrl")
            dartFormatClient = DartFormatClient(baseUrl)

            val httpResponse = dartFormatClient!!.get("/status")
            if (httpResponse.statusCode() != 200)
                throw Exception("Failed to start external dart_format: requested status but got: ${httpResponse.statusCode()} ${httpResponse.body()}")

            NotificationTools.notifyInfo(listOf("External dart_format started."), ProjectManager.getInstance().defaultProject)

            while (true)
            {
                val formatJob = channel.receive()
                Logger.log("$methodName: Got new job: ${formatJob.command}")

                if (!process.isAlive)
                    NotificationTools.notifyError("External dart_format died.", ProjectManager.getInstance().defaultProject)

                if (formatJob.command.toLowerCasePreservingASCIIRules() == "format")
                {
                    Logger.log("Calling format()")
                    formatJob.formatResult = formatViaExternalDartFormat(config = formatJob.config!!, inputText = formatJob.inputText!!)
                    Logger.log("Called format()")
                    Logger.log("Calling formatJob.complete()")
                    formatJob.complete()
                    Logger.log("Called formatJob.complete()")
                    continue
                }

                if (formatJob.command.toLowerCasePreservingASCIIRules() == "quit")
                {
                    Logger.log("Calling quit()")
                    formatJob.formatResult = quitExternalDartFormat()
                    Logger.log("Called quit()")
                    Logger.log("Calling formatJob.complete()")
                    formatJob.complete()
                    Logger.log("Called formatJob.complete()")
                    break
                }

                formatJob.formatResult = FormatResult.error("Unknown command: ${formatJob.command}")
                formatJob.complete()
            }

            Logger.log("$methodName: END")
        }
        catch (e: Exception)
        {
            Logger.logError("$methodName: Exception: $e")
            NotificationTools.reportThrowable(e, ProjectManager.getInstance().defaultProject)
        }
        catch (e: Error)
        {
            // necessary?
            Logger.logError("$methodName: Error: $e")
            NotificationTools.reportThrowable(e, ProjectManager.getInstance().defaultProject)
        }
    }

    fun formatViaChannel(inputText: String, config: String): FormatResult
    {
        val methodName = "$CLASS_NAME.formatViaChannel"
        Logger.log(methodName)
        val formatJob = FormatJob(command = "Format", inputText = inputText, config = config)

        try
        {
            runBlocking {
                withTimeout(Constants.WAIT_FOR_FORMAT_IN_SECONDS * 1000L) {
                    Logger.log("$methodName: sending")
                    channel.send(formatJob)
                    Logger.log("$methodName: sent.")
                    return@withTimeout "OK"
                }
            }

            runBlocking {
                withTimeout(Constants.WAIT_FOR_FORMAT_IN_SECONDS * 1000L) {
                    Logger.log("$methodName: joining")
                    formatJob.join()
                    Logger.log("$methodName: joined")
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

    private suspend fun quitExternalDartFormat() : FormatResult
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
            return FormatResult.throwable(methodName, e)
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
                Logger.log("$methodName: POST /format")
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

            val formattedText = withContext(Dispatchers.IO) {
                httpResponse.entity.content.readAllBytes()
            }.decodeToString()

            return FormatResult.ok(formattedText)
        }
        catch (e: Exception)
        {
            return FormatResult.throwable(methodName, e)
        }
        catch (e: Error)
        {
            // necessary?
            return FormatResult.throwable(methodName, e)
        }
    }
}
