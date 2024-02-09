package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.StringTools

data class ReadLineResponse(val stdOut: String?, val stdErr: String?)

class TimedReader
{
    companion object
    {
        private const val CLASS_NAME = "TimedReader"

        fun readLine(process: Process, inputStreamReader: StreamReader, errorStreamReader: StreamReader, timeoutInSeconds: Int, waitForName: String): ReadLineResponse?
        {
            val methodName = "$CLASS_NAME.readLine"
            Logger.logDebug("TimedReader.readLine()")

            var waitedMillis = 0
            while (waitedMillis < timeoutInSeconds * 1000)
            {
                val textFromInputStream = receiveLine(inputStreamReader)
                if (textFromInputStream != null)
                    return ReadLineResponse(textFromInputStream, null)

                val textFromErrorStream = receiveLine(errorStreamReader)
                if (textFromErrorStream != null)
                    return ReadLineResponse(null, textFromErrorStream)

                if (process.waitFor(Constants.WAIT_INTERVAL_IN_MILLIS.toLong(), java.util.concurrent.TimeUnit.MILLISECONDS))
                {
                    val title = "Unexpected process exit while waiting for $waitForName."

                    var content = ""
                    content += receiveLines(inputStreamReader, "\nStdOut: ") ?: ""
                    content += receiveLines(errorStreamReader, "\nStdErr: ") ?: ""
                    content = content.trim()

                    val checkInstallationInstructionsLink = NotificationTools.createCheckInstallationInstructionsLink()
                    val reportErrorLink = NotificationTools.createReportErrorLink(
                        content = content.ifEmpty { null },
                        gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                        origin = null,
                        stackTrace = null,
                        title = title
                    )

                    NotificationTools.notifyError(NotificationInfo(
                        content = content.ifEmpty { null },
                        fileName = null,
                        links = listOf(checkInstallationInstructionsLink, reportErrorLink),
                        origin = null,
                        project = null,
                        title = title
                    ))

                    return null
                }

                Thread.sleep(Constants.WAIT_INTERVAL_IN_MILLIS.toLong())
                waitedMillis += Constants.WAIT_INTERVAL_IN_MILLIS
            }

            Logger.logDebug("TimedReader.readResponse: waitedMillis: $waitedMillis")

            val errorText = "Timeout while waiting for response."
            Logger.logError("TimedReader.readLine: $errorText")
            throw DartFormatException.localError(errorText)
        }

        fun receiveLines(streamReader: StreamReader, prefix: String): String?
        {
            var r = ""
            while (true)
            {
                val s = receiveLine(streamReader) ?: break
                Logger.logDebug("TimedReader.receiveLines: Received: ${StringTools.toDisplayString(s, 100)}.")
                r += "$prefix$s"
            }

            return r.ifEmpty { null }
        }

        private fun receiveLine(streamReader: StreamReader): String?
        {
            val availableBytes = streamReader.available()
            if (availableBytes <= 0)
                return null

            Logger.logDebug("TimedReader.receiveLine: Receiving: $availableBytes bytes.")
            val s = streamReader.readLine()
            Logger.logDebug("TimedReader.receiveLine: Received: ${StringTools.toDisplayString(s, 100)}.")
            return s
        }
    }
}
