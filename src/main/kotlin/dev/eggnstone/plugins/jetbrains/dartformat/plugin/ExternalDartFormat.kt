package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.tools.JsonTools
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.OsTools
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

        while (true)
        {
            val formatJob = channel.receive()
            Logger.log("ExternalDartFormat.run: Got new job!")

            try
            {
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

                if (formatJob.command == "quit)")
                    break
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
            }
        }

        Logger.log("ExternalDartFormat.run: END $project")
    }

    fun format(project: Project, inputText: String): String
    {
        Logger.log("ExternalDartFormat.format")
        NotificationTools.notifyInfo(listOf("ExternalDartFormat.format: TODO"), project)
        val formatJob = FormatJob(command = "format", inputText = inputText)

        runBlocking {
            Logger.log("ExternalDartFormat.format: sending")
            channel.send(formatJob)
            Logger.log("ExternalDartFormat.format: sent")
            Logger.log("ExternalDartFormat.format: joining")
            formatJob.join() // TODO: timeout
            Logger.log("ExternalDartFormat.format: joined")
        }

        return formatJob.outputText ?: inputText
    }
}

