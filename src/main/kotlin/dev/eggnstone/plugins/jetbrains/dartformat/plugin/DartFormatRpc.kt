package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.enums.FailType
import dev.eggnstone.plugins.jetbrains.dartformat.tools.JsonTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import java.net.SocketTimeoutException

// Thin wrapper around the long-lived DartFormatClient HTTP connection. Holds no plugin state
// beyond the client; format() and quit() return FormatResult so the caller stays in charge of
// state transitions and notifications.
class DartFormatRpc(private val client: DartFormatClient)
{
    companion object
    {
        private const val CLASS_NAME = "DartFormatRpc"
    }

    suspend fun format(inputText: String, config: String, filePath: String): FormatResult
    {
        val methodName = "$CLASS_NAME.format"

        val inputBytes = inputText.toByteArray(Charsets.UTF_8).size
        if (inputBytes > Constants.MAX_FORMAT_INPUT_BYTES)
        {
            val inputMib = inputBytes / 1024.0 / 1024.0
            val limitMib = Constants.MAX_FORMAT_INPUT_BYTES / 1024 / 1024
            return FormatResult.error(
                "File too large for dart_format: %.2f MiB exceeds the %d MiB limit.".format(inputMib, limitMib)
            )
        }

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
                httpResponse = client.post("/format", entity)
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

            val statusCode = httpResponse.statusLine.statusCode
            if (statusCode != 200)
            {
                Logger.logDebug("$methodName: POST /format returned HTTP $statusCode")
                val message = when (statusCode)
                {
                    403 -> "dart_format rejected the request: Host check failed (must be a loopback address)."
                    411 -> "dart_format rejected the request: missing Content-Length header."
                    413 -> "dart_format rejected the request: body exceeds the ${Constants.MAX_FORMAT_INPUT_BYTES / 1024 / 1024} MiB limit."
                    else -> "dart_format returned HTTP $statusCode."
                }
                return FormatResult.error(message)
            }

            val result = withContext(Dispatchers.IO) { httpResponse.entity.content.readAllBytes() }.decodeToString()
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

    suspend fun quit(): FormatResult
    {
        val methodName = "$CLASS_NAME.quit"

        try
        {
            val httpResponse = client.get("/quit")
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
}
