package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.ReadLineResponse
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.StringTools

class TimedReader
{
    companion object
    {
        private const val CLASS_NAME = "TimedReader"

        fun readLine(process: Process, stdOutReader: StreamReader, stdErrReader: StreamReader, timeoutInSeconds: Int, waitForName: String): ReadLineResponse?
        {
            val methodName = "$CLASS_NAME.readLine"
            if (Constants.LOG_VERBOSE) Logger.logVerbose("$methodName()")

            var waitedMillis = 0
            while (timeoutInSeconds < 0 || waitedMillis < timeoutInSeconds * 1000)
            {
                val textFromStdOut = receiveLine(stdOutReader)
                if (textFromStdOut != null)
                    return ReadLineResponse(textFromStdOut, null)

                val textFromStdErr = receiveLine(stdErrReader)
                if (textFromStdErr != null)
                    return ReadLineResponse(null, textFromStdErr)

                if (process.waitFor(Constants.WAIT_INTERVAL_IN_MILLIS.toLong(), java.util.concurrent.TimeUnit.MILLISECONDS))
                {
                    val title = "Unexpected process exit while waiting for $waitForName."

                    var content = ""
                    content += receiveLines(stdOutReader, "\nStdOut: ") ?: ""
                    content += receiveLines(stdErrReader, "\nStdErr: ") ?: ""
                    content = content.trim()

                    if (content.isNotEmpty())
                        content += "\n"

                    content += "Did you install the dart_format package?\n" +
                        "Basically just execute this:<pre>dart pub global activate dart_format</pre>"

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
                        links = listOf(checkInstallationInstructionsLink, reportErrorLink),
                        origin = null,
                        project = null,
                        title = title,
                        virtualFile = null
                    ))

                    return null
                }

                Thread.sleep(Constants.WAIT_INTERVAL_IN_MILLIS.toLong())
                waitedMillis += Constants.WAIT_INTERVAL_IN_MILLIS
            }

            Logger.logDebug("$methodName: waitedMillis: $waitedMillis")

            val errorText = "Timeout while waiting for response."
            Logger.logError("$methodName: $errorText")
            throw DartFormatException.localError(errorText)
        }

        fun receiveLines(streamReader: StreamReader, prefix: String): String?
        {
            var r = ""
            while (true)
            {
                val s = receiveLine(streamReader) ?: break
                if (Constants.LOG_VERBOSE) Logger.logVerbose("TimedReader.receiveLines: Received: ${StringTools.toDisplayString(s, 100)}.")
                r += "$prefix$s"
            }

            return r.ifEmpty { null }
        }

        private fun receiveLine(streamReader: StreamReader): String?
        {
            val availableBytes = streamReader.available()
            if (availableBytes <= 0)
                return null

            if (Constants.LOG_VERBOSE) Logger.logVerbose("TimedReader.receiveLine: Receiving: $availableBytes bytes.")
            val s = streamReader.readLine()
            if (Constants.LOG_VERBOSE) Logger.logVerbose("TimedReader.receiveLine: Received: ${StringTools.toDisplayString(s, 100)}.")
            return s
        }
    }
}
