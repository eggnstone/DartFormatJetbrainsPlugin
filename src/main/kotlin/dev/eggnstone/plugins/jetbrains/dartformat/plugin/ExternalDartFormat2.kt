package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.pseudo_http.PseudoHttpClient
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import java.io.BufferedWriter

class ExternalDartFormat2(
    private val pseudoHttpClient: PseudoHttpClient,
    private val inputReader: StreamReader,
    private val errorReader: StreamReader,
    private val outputWriter: BufferedWriter,
    private val process: Process,
    private val project: Project
)
{
    fun format(inputText: String): FormatResult
    {
        try
        {
            Logger.log("ExternalDartFormat.format()")

            val result = pseudoHttpClient.post("/format", inputText)
            Logger.log("result: ${result.statusCode} ${result.status} ${result.headers}")

            /*var contentLength = -1
            var statusCode = -1
            var status = ""
            var isFirst = true
            while(true)
            {
                Logger.log("ExternalDartFormat.format: Calling inputReader.readLine() ...")
                val s = inputReader.readLine()
                Logger.log("ExternalDartFormat.format: Called inputReader.readLine(): $s")
                //Logger.log("ExternalDartFormat2.format: $s")
                if (s == "")
                    break

                if (isFirst)
                {
                    isFirst = false

                    if (!s.startsWith("HTTP/1.1 "))
                    {
                        val errorText = "Unexpected response: \"$s\""
                        Logger.logError("ExternalDartFormat.formatViaExternalDartFormat: $errorText")
                        return FormatResult.error(errorText)
                    }

                    statusCode = s.substring("HTTP/1.1 ".length, "HTTP/1.1 ".length + 3).toInt()
                    status = s.substring("HTTP/1.1 ".length + 4)
                    Logger.log("ExternalDartFormat.formatViaExternalDartFormat: statusCode: $statusCode status: $status")
                    continue
                }

                if (s.startsWith("Content-Length: "))
                {
                    contentLength = s.substring("Content-Length: ".length).toInt()
                    Logger.log("ExternalDartFormat.formatViaExternalDartFormat: contentLength: $contentLength")
                    continue
                }
            }*/

            return FormatResult.warning("TODO")
        }
        catch (e: Exception)
        {
            return FormatResult.error(e.message ?: "Unknown error")
        }
        /*catch (e: Error)
        {
            return FormatResult.error(e.message ?: "Unknown error")
        }*/
    }
}
