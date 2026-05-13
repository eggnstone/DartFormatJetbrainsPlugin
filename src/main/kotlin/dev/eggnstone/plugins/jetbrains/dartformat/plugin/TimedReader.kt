package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.ProcessExitInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.ReadLineResponse
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.StringTools
import java.util.concurrent.TimeUnit

class TimedReader
{
    companion object
    {
        private const val CLASS_NAME = "TimedReader"

        fun readLine(
            process: Process,
            stdOutReader: StreamReader,
            stdErrReader: StreamReader,
            timeoutInSeconds: Int,
            waitForName: String,
            onExit: ((ProcessExitInfo) -> Unit)? = null
        ): ReadLineResponse?
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

                if (process.waitFor(Constants.WAIT_INTERVAL_IN_MILLIS.toLong(), TimeUnit.MILLISECONDS))
                {
                    val exitInfo = captureExit(process, stdOutReader, stdErrReader)
                    if (onExit != null)
                        onExit(exitInfo)
                    else
                        notifyUnexpectedProcessExit(waitForName, exitInfo)
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
            val sb = StringBuilder()
            while (true)
            {
                val s = receiveLine(streamReader) ?: break
                if (Constants.LOG_VERBOSE) Logger.logVerbose("TimedReader.receiveLines: Received: ${StringTools.toDisplayString(s, 100)}.")
                sb.append(prefix).append(s)
            }

            return if (sb.isEmpty()) null else sb.toString()
        }

        // available() can return 0 right after process exit even when bytes remain in the pipe.
        // Drain to EOF so we actually surface stderr from crashed shims.
        private fun captureExit(process: Process, stdOutReader: StreamReader, stdErrReader: StreamReader): ProcessExitInfo
        {
            val precedingStdOut = receiveLines(stdOutReader, "\nStdOut: ") ?: ""
            val precedingStdErr = receiveLines(stdErrReader, "\nStdErr: ") ?: ""
            val tailStdOut = try { stdOutReader.drainToEof() } catch (_: Throwable) { "" }
            val tailStdErr = try { stdErrReader.drainToEof() } catch (_: Throwable) { "" }
            return ProcessExitInfo(
                stdOutTail = combineExitText(precedingStdOut, tailStdOut),
                stdErrTail = combineExitText(precedingStdErr, tailStdErr),
                exitCode = process.exitValue()
            )
        }

        fun notifyUnexpectedProcessExit(waitForName: String, exitInfo: ProcessExitInfo)
        {
            val title = "Unexpected process exit while waiting for $waitForName."

            val content = buildString {
                if (exitInfo.stdOutTail.isNotEmpty()) append("\nStdOut: ").append(exitInfo.stdOutTail)
                if (exitInfo.stdErrTail.isNotEmpty()) append("\nStdErr: ").append(exitInfo.stdErrTail)
                append("\nExitCode: ").append(exitInfo.exitCode)
            }.trim()
            Logger.logError("$CLASS_NAME.notifyUnexpectedProcessExit: $title\n$content")

            val finalContent = (if (content.isEmpty()) "" else "$content\n") +
                "Did you install the dart_format package?\n" +
                "Basically just execute this:<pre>dart pub global activate dart_format</pre>"

            val checkInstallationInstructionsLink = NotificationTools.createCheckInstallationInstructionsLink()
            val reportErrorLink = NotificationTools.createReportErrorLink(
                content = finalContent.ifEmpty { null },
                gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                origin = null,
                stackTrace = null,
                title = title
            )

            NotificationTools.notifyError(
                NotificationInfo(
                    content = finalContent.ifEmpty { null },
                    links = listOf(checkInstallationInstructionsLink, reportErrorLink),
                    origin = null,
                    project = null,
                    title = title,
                    virtualFile = null
                )
            )
        }

        private fun combineExitText(preceding: String, tail: String): String
        {
            val trimmedTail = tail.trimEnd()
            return if (trimmedTail.isEmpty()) preceding.trim() else (preceding + "\n" + trimmedTail).trim()
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
