package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.JsonTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.OsTools
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ExternalDartFormatter
{
    companion object
    {
        val instance = ExternalDartFormatter()
        private const val EXPECTED_PROTOCOL_VERSION = 1
    }

    private var mainJob: Job? = null
    private val channel = Channel<FormatJob>()

    @OptIn(DelicateCoroutinesApi::class)
    fun init(project: Project)
    {
        Logger.log("ExternalDartFormatter: init: $project")

        if (mainJob != null)
            return

        mainJob = GlobalScope.launch { run(project) }
    }

    private suspend fun run(project: Project)
    {
        Logger.log("ExternalDartFormatter.run: START $project")

        val processBuilder: ProcessBuilder = if (OsTools.isWindows())
            ProcessBuilder("cmd", "/c", "dart_format", "--plugin", "--errors-as-json")
        else
            ProcessBuilder("dart_format", "--plugin", "--errors-as-json")

        val process = withContext(Dispatchers.IO) {
            processBuilder.start()
        }

        val inputStream = process.inputStream
        val inputReader = inputStream.bufferedReader()
        val outputStream = process.outputStream
        val outputWriter = outputStream.bufferedWriter()

        val json = withContext(Dispatchers.IO) {
            inputReader.readLine()
        }

        Logger.log("ExternalDartFormatter: $json")
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
            Logger.log("ExternalDartFormatter.run: Got new job!")

            try
            {
                val command = """{"Command": "PrepareToReceive", "ContentLength": ${formatJob.inputText.length}}"""
                Logger.log("ExternalDartFormatter: command: $command")

                withContext(Dispatchers.IO) {
                    outputWriter.write(command + "\n")
                    outputWriter.flush()
                }

                var commandResponse = ResponseReader.readResponse(process)
                Logger.log("ExternalDartFormatter: commandResponse: $commandResponse")

                if (commandResponse.source == "ERROR")
                {
                    val errorText = "TODO: ERROR"

                    Logger.log("ExternalDartFormatter.run: notifying")
                    formatJob.complete()
                    Logger.log("ExternalDartFormatter.run: notified")

                    Logger.logError(errorText)
                    NotificationTools.notifyError(errorText, project)
                    continue
                }

                if (commandResponse.statusCode != 201)
                {
                    val errorText = "TODO: ERROR: statusCode: ${commandResponse.statusCode} status: ${commandResponse.status}"

                    Logger.log("ExternalDartFormatter.run: notifying")
                    formatJob.complete()
                    Logger.log("ExternalDartFormatter.run: notified")

                    Logger.logError(errorText)
                    NotificationTools.notifyError(errorText, project)
                    continue
                }

                Logger.log("ExternalDartFormatter: commandResponse: $commandResponse")

                withContext(Dispatchers.IO) {
                    outputWriter.write(formatJob.inputText)
                    outputWriter.flush()
                }

                commandResponse = ResponseReader.readResponse(process, true)
                Logger.log("ExternalDartFormatter: commandResponse: $commandResponse")

                if (commandResponse.source == "ERROR")
                {
                    val errorText = "TODO: ERROR"

                    Logger.log("ExternalDartFormatter.run: notifying")
                    formatJob.complete()
                    Logger.log("ExternalDartFormatter.run: notified")

                    Logger.logError(errorText)
                    NotificationTools.notifyError(errorText, project)
                    continue
                }

                if (commandResponse.statusCode != 202)
                {
                    val errorText = "TODO: ERROR: statusCode: ${commandResponse.statusCode} status: ${commandResponse.status}"

                    Logger.log("ExternalDartFormatter.run: notifying")
                    formatJob.complete()
                    Logger.log("ExternalDartFormatter.run: notified")

                    Logger.logError(errorText)
                    NotificationTools.notifyError(errorText, project)
                    continue
                }

                commandResponse = ResponseReader.readResponse(process)
                Logger.log("ExternalDartFormatter: commandResponse: $commandResponse")

                if (commandResponse.source == "ERROR")
                {
                    val errorText = "TODO: ERROR"

                    Logger.log("ExternalDartFormatter.run: notifying")
                    formatJob.complete()
                    Logger.log("ExternalDartFormatter.run: notified")

                    Logger.logError(errorText)
                    NotificationTools.notifyError(errorText, project)
                    continue
                }

                if (commandResponse.statusCode != 203)
                {
                    val errorText = "TODO: ERROR: statusCode: ${commandResponse.statusCode} status: ${commandResponse.status}"

                    Logger.log("ExternalDartFormatter.run: notifying")
                    formatJob.complete()
                    Logger.log("ExternalDartFormatter.run: notified")

                    Logger.logError(errorText)
                    NotificationTools.notifyError(errorText, project)
                    continue
                }

                formatJob.outputText = "/*TODO*/\n${formatJob.inputText}"

                Logger.log("ExternalDartFormatter.run: notifying")
                formatJob.complete()
                Logger.log("ExternalDartFormatter.run: notified")

                if (formatJob.command == "quit)")
                    break
            }
            catch (e: DartFormatException)
            {
                NotificationTools.reportThrowable(e, project)
                formatJob.complete()
            }
        }

        Logger.log("ExternalDartFormatter.run: END $project")
    }

    fun format(project: Project, inputText: String): String
    {
        Logger.log("ExternalDartFormatter.format")
        NotificationTools.notifyInfo(listOf("ExternalDartFormatter.format: TODO"), project)
        val formatJob = FormatJob(command = "format", inputText = inputText)

        runBlocking {
            Logger.log("ExternalDartFormatter.format: sending")
            channel.send(formatJob)
            Logger.log("ExternalDartFormatter.format: sent")
            Logger.log("ExternalDartFormatter.format: joining")
            formatJob.join() // TODO: timeout
            Logger.log("ExternalDartFormatter.format: joined")
        }

        return formatJob.outputText ?: inputText
    }
}

