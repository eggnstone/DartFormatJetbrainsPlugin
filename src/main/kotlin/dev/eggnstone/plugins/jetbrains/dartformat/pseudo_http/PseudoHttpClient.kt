package dev.eggnstone.plugins.jetbrains.dartformat.pseudo_http

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.StringTools
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.io.BufferedWriter
import java.io.OutputStreamWriter

class PseudoHttpClient(private val inputReader: StreamReader, private val outputWriter: BufferedWriter, private val outputWriter1: OutputStreamWriter)
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
        Logger.log("PseudoHttpClient.get: flushing outputWriter1 ...")
        outputWriter1.flush()
        Logger.log("PseudoHttpClient.get: flushing outputWriter ...")
        outputWriter.flush()
        Logger.log("PseudoHttpClient.get: wrote to outputWriter.")

        return readResponse()
    }

    fun post(path: String, text: String): PseudoHttpResult
    {
        Logger.log("PseudoHttpClient.post($path)")

        try
        {
            val y: Any = runBlocking {
                try
                {
                    val x: Any = withTimeout(Constants.WAIT_FOR_FORMAT_IN_SECONDS * 1000L) {
                        try
                        {
                            Logger.log("PseudoHttpClient.post: writing to outputWriter ...")
                            outputWriter.write("POST $path $PROTOCOL_AND_VERSION\n")
                            outputWriter.write("User-Agent: DartFormatPlugin\n")
                            outputWriter.write("Content-Type: text/plain; charset=utf-8\n")
                            outputWriter.write("Content-Length: ${text.length}\n")
                            outputWriter.write("Config: {}\n")
                            outputWriter.write("\n")
                            outputWriter.write(text)
                            Logger.log("PseudoHttpClient.post: wrote to outputWriter.")
                            Logger.log("PseudoHttpClient.post: flushing outputWriter ...")
                            outputWriter.flush()
                            Logger.log("PseudoHttpClient.post: flushed outputWriter.")

                            return@withTimeout "OK"
                        }
                        catch (e: TimeoutCancellationException)
                        {
                            val errorText = "Timeout while writing to outputWriter."
                            Logger.logError("PseudoHttpClient.post: $errorText")
                            return@withTimeout errorText
                        }
                        catch (e: Exception)
                        {
                            val errorText = "Exception while writing to outputWriter: $e"
                            Logger.logError("PseudoHttpClient.post: $errorText")
                            return@withTimeout errorText
                        }
                        catch (e: Error)
                        {
                            val errorText = "Error while writing to outputWriter: $e"
                            Logger.logError("PseudoHttpClient.post: $errorText")
                            return@withTimeout errorText
                        }
                    }

                    return@runBlocking x
                }
                catch (e: TimeoutCancellationException)
                {
                    val errorText = "Timeout while writing to outputWriter."
                    Logger.logError("PseudoHttpClient.post: $errorText")
                    return@runBlocking "Timeout"
                }
                catch (e: Exception)
                {
                    val errorText = "Exception while writing to outputWriter: $e"
                    Logger.logError("PseudoHttpClient.post: $errorText")
                    return@runBlocking errorText
                }
                catch (e: Error)
                {
                    val errorText = "Error while writing to outputWriter: $e"
                    Logger.logError("PseudoHttpClient.post: $errorText")
                    return@runBlocking errorText
                }
            }

            return PseudoHttpResult(500, y.toString(), mutableListOf())
        }
        catch (e: TimeoutCancellationException)
        {
            val errorText = "Timeout while writing to outputWriter."
            Logger.logError("PseudoHttpClient.post: $errorText")
            return PseudoHttpResult(500, errorText, mutableListOf())
        }
        catch (e: Exception)
        {
            val errorText = "Exception while writing to outputWriter: $e"
            Logger.logError("PseudoHttpClient.post: $errorText")
            return PseudoHttpResult(500, errorText, mutableListOf())
        }
        catch (e: Error)
        {
            val errorText = "Error while writing to outputWriter: $e"
            Logger.logError("PseudoHttpClient.post: $errorText")
            return PseudoHttpResult(500, errorText, mutableListOf())
        }

        return readResponse()
    }

    private fun readResponse(): PseudoHttpResult
    {
        Logger.log("PseudoHttpClient.readResponse()")

        try
        {
            val headers = mutableListOf<String>()
            val body = ByteArray(0)

            var s: String
            while (true)
            {
                Logger.log("PseudoHttpClient.readResponse: Calling inputReader.readLine() ...")
                s = inputReader.readLine()
                Logger.log("PseudoHttpClient.readResponse: Called inputReader.readLine(): ${StringTools.toDisplayString(s)}")

                if (s == "")
                {
                    Logger.log("PseudoHttpClient.readResponse: Ignoring empty line. TODO: fix this.")
                    continue
                }

                if (s.startsWith(RESPONSE_PREFIX))
                    break

                val errorText = "Unexpected response: \"$s\""
                Logger.logError("PseudoHttpClient.readResponse: $errorText")
                return PseudoHttpResult(500, errorText, headers)
            }

            val statusCode = s.substring(RESPONSE_PREFIX.length, RESPONSE_PREFIX.length + EXPECTED_STATUS_CODE_LENGTH).toInt()
            val status = s.substring(RESPONSE_PREFIX.length + EXPECTED_STATUS_CODE_LENGTH + 1)

            while (s != "")
            {
                headers.add(s)

                Logger.log("PseudoHttpClient.readResponse: Calling inputReader.readLine() ...")
                s = inputReader.readLine()
                Logger.log("PseudoHttpClient.readResponse: Called inputReader.readLine(): ${StringTools.toDisplayString(s)}")
            }

            Logger.log("PseudoHttpClient.readResponse: Received empty line. Returning PseudoHttpResult.")
            return PseudoHttpResult(statusCode, status, headers, body)
        }
        catch (e: Exception)
        {
            Logger.logError("PseudoHttpClient.readResponse: $e")
            return PseudoHttpResult(500, e.toString(), mutableListOf())
        }
    }
}
