package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.OsTools
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.future.await
import org.intellij.markdown.html.urlEncode
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

class ExternalDartFormat
{
    companion object
    {
        val instance = ExternalDartFormat()
    }

    private var mainJob: Job? = null
    private val channel = Channel<FormatJob>()

    @OptIn(DelicateCoroutinesApi::class)
    fun init(project: Project)
    {
        val methodName = "ExternalDartFormat.init"
        Logger.log("$methodName: $project")

        if (mainJob != null)
            return

        mainJob = GlobalScope.launch { run(project) }
    }

    private suspend fun run(firstProject: Project)
    {
        val methodName = "ExternalDartFormat.run"
        Logger.log("$methodName: START $firstProject")

        try
        {
            val processBuilder: ProcessBuilder = if (OsTools.isWindows())
                ProcessBuilder("cmd", "/c", "dart_format", "--web", "--errors-as-json")
            else
                ProcessBuilder("dart_format", "--web", "--errors-as-json")

            val process = withContext(Dispatchers.IO) {
                processBuilder.start()
            }

            if (!process.isAlive)
                throw Exception("Failed to start external dart_format: process is dead.")

            Logger.log("$methodName: External dart_format started.")

            val inputReader = StreamReader(process.inputStream)
            val baseUrl = TimedReader.readLine(process, inputReader, Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_START_IN_SECONDS)
            Logger.log("$methodName: baseUrl: $baseUrl")

            @Suppress("HttpUrlsUsage")
            if (!baseUrl.startsWith("http://"))
                throw Exception("Failed to start external dart_format: expected URL but got: $baseUrl")

            val httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("$baseUrl/status"))
                .header("User-Agent", "DartFormatPlugin")
                .header("Content-Type", "text/plain; charset=utf-8")
                .GET()
                .build()

            val httpResponse = HttpClient.newHttpClient().sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).await()
            Logger.log("$methodName: httpResponse: $httpResponse")
            if (httpResponse.statusCode() != 200)
                throw Exception("Failed to start external dart_format: requested status but got: ${httpResponse.statusCode()} ${httpResponse.body()}")

            while (true)
            {
                val formatJob = channel.receive()
                Logger.log("$methodName: Got new job: ${formatJob.command}")

                if (formatJob.command.toLowerCasePreservingASCIIRules() == "format")
                {
                    Logger.log("Calling format()")
                    formatJob.formatResult = formatViaExternalDartFormat(baseUrl = baseUrl, config = formatJob.config!!, inputText = formatJob.inputText!!)
                    Logger.log("Called format()")
                    Logger.log("Calling formatJob.complete()")
                    formatJob.complete()
                    Logger.log("Called formatJob.complete()")
                    continue
                }

                if (formatJob.command.toLowerCasePreservingASCIIRules() == "quit")
                {
                    formatJob.complete()
                    break
                }

                formatJob.formatResult = FormatResult.error("Unknown command: ${formatJob.command}")
                formatJob.complete()
            }

            Logger.log("$methodName: END $firstProject")
        }
        catch (e: Exception)
        {
            Logger.logError("$methodName: Exception: $e")
            NotificationTools.reportThrowable(e, ProjectManager.getInstance().defaultProject)
        }
        catch (e: Error)
        {
            Logger.logError("$methodName: Error: $e")
            NotificationTools.reportThrowable(e, ProjectManager.getInstance().defaultProject)
        }
    }

    fun formatViaChannel(inputText: String, config: String): FormatResult
    {
        Logger.log("ExternalDartFormat.format")
        val formatJob = FormatJob(command = "Format", inputText = inputText, config = config)

        try
        {
            runBlocking {
                withTimeout(Constants.WAIT_FOR_FORMAT_IN_SECONDS * 1000L) {
                    Logger.log("ExternalDartFormat.format: sending")
                    channel.send(formatJob)
                    Logger.log("ExternalDartFormat.format: sent.")
                    return@withTimeout "OK"
                }
            }

            runBlocking {
                withTimeout(Constants.WAIT_FOR_FORMAT_IN_SECONDS * 1000L) {
                    Logger.log("ExternalDartFormat.format: joining")
                    formatJob.join()
                    Logger.log("ExternalDartFormat.format: joined")
                    return@withTimeout "OK"
                }
            }
        }
        catch (e: TimeoutCancellationException)
        {
            formatJob.cancel()
            val errorText = "Timeout while waiting for external dart_format."
            Logger.logError("ExternalDartFormat.format: $errorText")
            return FormatResult.error(errorText)
        }

        return formatJob.formatResult ?: FormatResult.error("No result")
    }

    private suspend fun formatViaExternalDartFormat(inputText: String, baseUrl: String, config: String): FormatResult
    {
        val methodName = "ExternalDartFormat.format"

        try
        {
            val safeConfig = urlEncode(config)
            val httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("$baseUrl/format?Config=$safeConfig"))
                .header("User-Agent", "DartFormatPlugin")
                .header("Content-Type", "text/plain; charset=utf-8")
                .POST(BodyPublishers.ofByteArray(inputText.toByteArray()))
                .build()

            Logger.log("$methodName: 1")
            val httpResponse = HttpClient.newHttpClient().sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).await()
            Logger.log("$methodName: 2")
            Logger.log("$methodName: httpResponse: $httpResponse")
            if (httpResponse.statusCode() != 200)
                throw Exception("Failed to format via external dart_format: ${httpResponse.statusCode()} ${httpResponse.body()}")

            return FormatResult.warning("TODO")
        }
        catch (e: Exception)
        {
            return FormatResult.throwable(methodName, e)
        }
    }
}
