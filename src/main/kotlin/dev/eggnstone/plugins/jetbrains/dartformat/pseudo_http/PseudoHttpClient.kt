package dev.eggnstone.plugins.jetbrains.dartformat.pseudo_http

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import java.io.BufferedWriter

class PseudoHttpClient(private val inputReader: StreamReader, private val outputWriter: BufferedWriter)
{
    companion object
    {
        private const val PROTOCOL_AND_VERSION = "PseudoHttp/1.0"
        private const val RESPONSE_PREFIX = "$PROTOCOL_AND_VERSION "
        private const val EXPECTED_STATUS_CODE_LENGTH = 3
    }

    fun get(path: String): PseudoHttpResult
    {

        
        Logger.log("PseudoHttpClient.get($path)")
        Logger.log("PseudoHttpClient.get: writing to outputWriter ...")
        outputWriter.write("GET $path $PROTOCOL_AND_VERSION\n")
        outputWriter.write("User-Agent: DartFormatPlugin\n")
        outputWriter.write("Protocol-Version: ${Constants.PROTOCOL_VERSION}\n")
        outputWriter.write("\n")
        outputWriter.flush()
        Logger.log("PseudoHttpClient.get: wrote to outputWriter.")

        return readResponse()
    }

    fun post(path: String, text: String): PseudoHttpResult
    {
        Logger.log("PseudoHttpClient.post($path)")
        Logger.log("PseudoHttpClient.post: writing to outputWriter ...")
        outputWriter.write("POST $path $PROTOCOL_AND_VERSION\n")
        outputWriter.write("User-Agent: DartFormatPlugin\n")
        outputWriter.write("Content-Type: text/plain; charset=utf-8\n")
        outputWriter.write("Content-Length: ${text.length}\n")
        outputWriter.write("Config: {}\n")
        outputWriter.write("\n")
        outputWriter.write(text)
        outputWriter.flush()
        Logger.log("PseudoHttpClient.post: wrote to outputWriter.")

        return readResponse()
    }

    private fun readResponse(): PseudoHttpResult
    {
        try
        {
            val headers = mutableListOf<String>()
            val body = ByteArray(0)

            Logger.log("PseudoHttpClient.readResponse: Calling inputReader.readLine() ...")
            var s = inputReader.readLine()
            Logger.log("PseudoHttpClient.readResponse: Called inputReader.readLine(): $s")
            if (!s.startsWith(RESPONSE_PREFIX))
            {
                val errorText = "Unexpected response: \"$s\""
                Logger.logError("PseudoHttpClient.readResponse: $errorText")
                return PseudoHttpResult(500, errorText, headers)
            }

            val statusCode = s.substring(RESPONSE_PREFIX.length, RESPONSE_PREFIX.length + EXPECTED_STATUS_CODE_LENGTH).toInt()
            val status = s.substring(RESPONSE_PREFIX.length + EXPECTED_STATUS_CODE_LENGTH + 1)

            while (s != "")
            {
                Logger.log("PseudoHttpClient.readResponse: readLine: $s")
                headers.add(s)

                s = inputReader.readLine()
            }

            return PseudoHttpResult(statusCode, status, headers, body)
        }
        catch (e: Exception)
        {
            Logger.logError("PseudoHttpClient.readResponse: $e")
            return PseudoHttpResult(500, e.toString(), mutableListOf())
        }
    }
}
