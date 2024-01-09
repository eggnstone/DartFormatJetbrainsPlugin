package dev.eggnstone.plugins.jetbrains.dartformat.plugin

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
import kotlinx.coroutines.future.await
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.intellij.markdown.html.urlEncode
import java.net.SocketTimeoutException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
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

        mainJob = GlobalScope.launch { run() }
    }

    private suspend fun run()
    {
        val methodName = "ExternalDartFormat.run"
        Logger.log("$methodName: START")

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
            val baseUrl2 = TimedReader.readLine(process, inputReader, Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_START_IN_SECONDS)
            Logger.log("$methodName: baseUrl: $baseUrl2")
            val baseUrl = "http://127.0.0.1:8008"

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
            //Logger.log("$methodName: httpResponse: $httpResponse")
            if (httpResponse.statusCode() != 200)
                throw Exception("Failed to start external dart_format: requested status but got: ${httpResponse.statusCode()} ${httpResponse.body()}")

            //NotificationTools.notifyInfo(listOf("External dart_format started."), ProjectManager.getInstance().defaultProject)

            while (true)
            {
                val formatJob = channel.receive()
                Logger.log("$methodName: Got new job: ${formatJob.command}")

                if (formatJob.command.toLowerCasePreservingASCIIRules() == "format")
                {
                    Logger.log("Calling format()")
                    formatJob.formatResult = formatViaExternalDartFormat(baseUrl = baseUrl, config = formatJob.config!!, inputText = formatJob.inputText!!)
                    Logger.log("Called format()")
                    //NotificationTools.notifyInfo(listOf("FORMATTED"), ProjectManager.getInstance().defaultProject)
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

        //Logger.log("formatJob.formatResult: ${formatJob.formatResult?.text}")
        return formatJob.formatResult ?: FormatResult.error("No result")
    }

    private suspend fun formatViaExternalDartFormat(inputText: String, baseUrl: String, config: String): FormatResult
    {
        val methodName = "ExternalDartFormat.format"

        try
        {
            val requestConfig = RequestConfig.custom()
                .setConnectTimeout(Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_RESPONSE_IN_SECONDS * 1000)
                .setConnectionRequestTimeout(Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_RESPONSE_IN_SECONDS * 1000)
                .setSocketTimeout(Constants.WAIT_FOR_EXTERNAL_DART_FORMAT_RESPONSE_IN_SECONDS * 1000).build()
            val httpClient: CloseableHttpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build()
            val safeConfig = urlEncode(config)
            val httpRequest = HttpPost("$baseUrl/format?Config=$safeConfig")
            httpRequest.entity = ByteArrayEntity(inputText.toByteArray())
            val httpResponse: CloseableHttpResponse
            try
            {
                Logger.log("$methodName: POST /format")
                httpResponse = httpClient.execute(httpRequest, null)
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

            //Logger.log("$methodName: formattedText: $formattedText")

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
