package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.tools.JsonTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.OsTools
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ExternalDartFormat
{
    companion object
    {
        val instance = ExternalDartFormat()
        private const val EXPECTED_PROTOCOL_VERSION = 1
    }

    private var mainJob: Job? = null
    private val channel = Channel<FormatJob>()

    @OptIn(DelicateCoroutinesApi::class)
    fun init(project: Project)
    {
        Logger.log("ExternalDartFormat: init: $project")

        if (mainJob != null)
            return

        mainJob = GlobalScope.launch { run(project) }
    }

    private suspend fun run(project: Project)
    {
        Logger.log("ExternalDartFormat.run: START $project")

        val processBuilder: ProcessBuilder = if (OsTools.isWindows())
            ProcessBuilder("cmd", "/c", "dart_format", "--plugin", "--errors-as-json")
        else
            ProcessBuilder("dart_format", "--plugin", "--errors-as-json")

        val process = withContext(Dispatchers.IO) {
            processBuilder.start()
        }

        val inputReader = StreamReader(process.inputStream)
        val errorReader = StreamReader(process.errorStream)
        val outputWriter = process.outputStream.bufferedWriter()

        // TODO: replace with HTTP-like command
        val json = inputReader.readLine()
        Logger.log("ExternalDartFormat: $json")
        val jsonElement = JsonTools.parse(json)
        val statusCode = JsonTools.getInt(jsonElement, "StatusCode", -1)
        val status = JsonTools.getString(jsonElement, "Status", "?")
        if (statusCode != 200)
        {
            val errorText = "Failed to start external dart_format: $statusCode $status"
            Logger.logError(errorText)
            NotificationTools.notifyError(errorText, project)
            return
        }

        val protocolVersion = JsonTools.getInt(jsonElement, "ProtocolVersion", -1)
        if (protocolVersion != EXPECTED_PROTOCOL_VERSION)
        {
            val errorText = "External dart_format: expected protocol version $EXPECTED_PROTOCOL_VERSION, got $protocolVersion"
            Logger.logError(errorText)
            NotificationTools.notifyError(errorText, project)
        }

        val externalDartFormat2 = ExternalDartFormat2(inputReader, errorReader, outputWriter, process, project)

        while (true)
        {
            val formatJob = channel.receive()
            Logger.log("ExternalDartFormat.run: Got new job: ${formatJob.command}")

            if (formatJob.command.toLowerCasePreservingASCIIRules() == "format")
            {
                Logger.log("Calling externalDartFormat2.format()")
                formatJob.formatResult = externalDartFormat2.format(formatJob.inputText)
                Logger.log("Called externalDartFormat2.format()")
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

        Logger.log("ExternalDartFormat.run: END $project")

        /*@Suppress("UNREACHABLE_CODE")
        try
        {
            if (formatJob.command == "quit)")
                break

            formatViaExternalDartFormat(formatJob, process, inputReader, errorReader, outputWriter, project)
            continue

            Logger.log("formatJob.inputText.length: ${formatJob.inputText.length}")
            val inputText = formatJob.inputText
            val inputLength = inputText.length
            var command = """{"Command": "PrepareToReceive", "ContentLength": ${inputLength}}"""
            Logger.log("ExternalDartFormat: command: $command")

            withContext(Dispatchers.IO) {
                outputWriter.write(command + "\n")
                outputWriter.flush()
            }

            var commandResponse = ResponseReader.readResponse(process, inputReader, errorReader)
            Logger.log("ExternalDartFormat: commandResponse 1: $commandResponse")

            if (commandResponse.statusCode != 201)
            {
                val errorText = "TODO: ERROR: statusCode: ${commandResponse.statusCode} status: ${commandResponse.status}"
                Logger.logError(errorText)
                NotificationTools.notifyError(errorText, project)
                formatJob.errorText = errorText
                continue
            }

            commandResponse = ResponseReader.readResponse(process, inputReader, errorReader)
            Logger.log("ExternalDartFormat: commandResponse 1a: $commandResponse")


            Logger.log("ExternalDartFormat: Sending inputText ...")
            withContext(Dispatchers.IO) {
                Logger.log("ExternalDartFormat: Sending inputText START")

                var s = inputText
                while (s.length > 1000)
                {
                    Logger.log("ExternalDartFormat: Sending inputText 2 a")
                    outputWriter.write(s.substring(0, 1000))
                    Logger.log("ExternalDartFormat: Sending inputText 2 b")
                    Logger.log("ExternalDartFormat: Sending inputText 2 c")
                    outputWriter.flush()
                    Logger.log("ExternalDartFormat: Sending inputText 2 d")
                    commandResponse = ResponseReader.readResponse(process, inputReader, errorReader)
                    Logger.log("ExternalDartFormat: commandResponse 2: $commandResponse")
                    s = s.substring(1000)
                }

                Logger.log("ExternalDartFormat: Sending inputText 2 r a ${s.length}")
                outputWriter.write(s)
                Logger.log("ExternalDartFormat: Sending inputText 2 r b")
                outputWriter.flush()
                Logger.log("ExternalDartFormat: Sending inputText 2 r c")

                Logger.log("ExternalDartFormat: Sending inputText END")
            }
            Logger.log("ExternalDartFormat: Sent inputText.")

            do
            {
                commandResponse = ResponseReader.readResponse(process, inputReader, errorReader)
                Logger.log("ExternalDartFormat: commandResponse 2: $commandResponse")
            } while (commandResponse.statusCode == 100)

            if (commandResponse.statusCode != 202)
            {
                val errorText = "TODO: ERROR: statusCode: ${commandResponse.statusCode} status: ${commandResponse.status}"
                Logger.logError(errorText)
                NotificationTools.notifyError(errorText, project)
                formatJob.errorText = errorText
                continue
            }

            do
            {
                commandResponse = ResponseReader.readResponse(process, inputReader, errorReader)
                Logger.log("ExternalDartFormat: commandResponse 3: $commandResponse")
            } while (commandResponse.statusCode == 100)

            if (commandResponse.statusCode != 203)
            {
                val errorText = "TODO: ERROR: statusCode: ${commandResponse.statusCode} status: ${commandResponse.status}"
                Logger.logError(errorText)
                NotificationTools.notifyError(errorText, project)
                formatJob.errorText = errorText
                continue
            }

            val resultLength = commandResponse.contentLength
            if (resultLength == null || resultLength <= 0)
            {
                val errorText = "TODO: ERROR: statusCode: ${commandResponse.statusCode} status: ${commandResponse.status}"
                Logger.logError(errorText)
                NotificationTools.notifyError(errorText, project)
                formatJob.errorText = errorText
                continue
            }

            command = """{"Command": "RetrieveResult"}"""
            Logger.log("ExternalDartFormat: command: $command")

            withContext(Dispatchers.IO) {
                outputWriter.write(command + "\n")
                outputWriter.flush()
            }

            Logger.log("ExternalDartFormat.run: Calling read($resultLength) ...")
            val result = inputReader.read(resultLength)
            Logger.log("ExternalDartFormat.run: Called read($resultLength).")

            if (result.length != resultLength)
            {
                val errorText = "result.length != resultLength: ${result.length} != $resultLength"
                Logger.logError(result)
                Logger.logError(errorText)
                NotificationTools.notifyError(errorText, project)
                formatJob.errorText = errorText
                continue
            }

            formatJob.outputText = result
        }
        catch (e: Exception)
        {
            Logger.logError("ExternalDartFormat.run: Exception: $e")
            NotificationTools.reportThrowable(e, project)
            formatJob.errorText = e.toString()
        }
        catch (e: Error)
        {
            Logger.logError("ExternalDartFormat.run: Error: $e")
            NotificationTools.reportThrowable(e, project)
            formatJob.errorText = e.toString()
        }
        finally
        {
            Logger.log("ExternalDartFormat.run: Calling formatJob.complete()")
            formatJob.complete()
            Logger.log("ExternalDartFormat.run: Called formatJob.complete()")
        }*/
    }

    fun format(project: Project, inputText: String): FormatResult
    {
        Logger.log("ExternalDartFormat.format")
        val formatJob = FormatJob(command = "format", inputText = inputText)

        try
        {
            runBlocking {
                 withTimeout(Constants.WAIT_FOR_FORMAT_IN_SECONDS * 1000L) {
                    Logger.log("ExternalDartFormat.format: sending")
                    channel.send(formatJob)
                    Logger.log("ExternalDartFormat.format: sent")
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

    /*private fun formatViaExternalDartFormat(formatJob: FormatJob, process: Process, inputReader: StreamReader, errorReader: StreamReader, outputWriter: BufferedWriter, project: Project)
    {
        Logger.log("ExternalDartFormat.formatViaExternalDartFormat()")

        outputWriter.write("POST / HTTP/1.1\n")
        outputWriter.write("User-Agent: DartFormatPlugin\n")
        outputWriter.write("Content-Type: text/plain; charset=utf-8\n")
        outputWriter.write("Content-Length: ${formatJob.inputText.length}\n")
        outputWriter.write("Config: {}\n")
        outputWriter.write("\n")
        outputWriter.write(formatJob.inputText)

        var contentLength = -1
        var statusCode = -1
        var status = ""
        var isFirst = true
        while(true)
        {
            val s = inputReader.readLine()
            Logger.log("ExternalDartFormat.formatViaExternalDartFormat: $s")
            if (s == "")
                break

            if (isFirst)
            {
                isFirst = false

                if (!s.startsWith("HTTP/1.1 "))
                {
                    val errorText = "Unexpected response: \"$s\""
                    Logger.logError("ExternalDartFormat.formatViaExternalDartFormat: $errorText")
                    formatJob.errorText = errorText
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
        }

        formatJob.outputText = formatJob.inputText
    }*/
}
