package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.util.io.awaitExit
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.enums.ExternalDartFormatState
import dev.eggnstone.plugins.jetbrains.dartformat.process.ProcessInfoOrLocalError
import dev.eggnstone.plugins.jetbrains.dartformat.process.ProcessTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.ExternalDartFormatTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.OsTools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DartFormatInstaller
{
    companion object
    {
        // Runs `dart pub global activate dart_format` once and reports the outcome via notifications.
        // setState is invoked with FAILED_TO_INSTALL / FAILED_TO_UPDATE before throwing on
        // configuration-level errors (HOME / SHELL missing); the caller's outer catch handler
        // can rely on `state` already reflecting the failure when the exception arrives.
        suspend fun tryInstall(shouldUpdate: Boolean, setState: (ExternalDartFormatState) -> Unit): Boolean
        {
            val actionEdLower = if (shouldUpdate) "updated" else "installed"
            val actionErUpper = if (shouldUpdate) "Updater" else "Installer"
            val actionIngUpper = if (shouldUpdate) "Updating" else "Installing"
            val actionLower = if (shouldUpdate) "update" else "install"

            val installProcessBuilderInfo = when (val installInfo = ExternalDartFormatTools.getInstallExternalDartFormatInfo())
            {
                is ProcessInfoOrLocalError.LocalError ->
                {
                    setState(if (shouldUpdate) ExternalDartFormatState.FAILED_TO_UPDATE else ExternalDartFormatState.FAILED_TO_INSTALL)
                    throw DartFormatException.localError("Unexpected error: ${installInfo.error.message}")
                }

                is ProcessInfoOrLocalError.Normal -> installInfo.processBuilderInfo
            }

            val processBuilder = ProcessTools.createProcessBuilder(installProcessBuilderInfo)

            Logger.logDebug("$actionIngUpper external dart_format: ${processBuilder.command().joinToString(separator = " ")}")
            NotificationTools.notifyInfo(
                NotificationInfo(
                    content = null,
                    links = null,
                    origin = null,
                    project = null,
                    title = "$actionIngUpper external dart_format ...\nThis may take a few seconds.",
                    virtualFile = null
                )
            )

            if (Constants.LOG_VERBOSE) Logger.logVerbose("$actionIngUpper external dart_format: Process starting ...")
            val process: Process = withContext(Dispatchers.IO) { processBuilder.start() }
            if (Constants.LOG_VERBOSE) Logger.logVerbose("$actionIngUpper external dart_format: Process started.")

            var exitCode: Int
            val processWasAlive = process.isAlive
            if (processWasAlive)
            {
                if (Constants.DEBUG_CONNECTION) NotificationTools.notifyInfo(
                    NotificationInfo(
                        content = null,
                        links = null,
                        origin = null,
                        project = null,
                        title = "$actionErUpper for external dart_format process is alive.\nWaiting for process to finish ...",
                        virtualFile = null
                    )
                )

                if (Constants.LOG_VERBOSE) Logger.logVerbose("$actionIngUpper external dart_format: Waiting for process to finish ...")
                exitCode = process.awaitExit()
                if (Constants.LOG_VERBOSE) Logger.logVerbose("$actionIngUpper external dart_format: Process finished. Exit code: $exitCode")
            }
            else
            {
                exitCode = try
                {
                    process.awaitExit()
                }
                catch (_: Exception)
                {
                    -1
                }

                Logger.logDebug("$actionErUpper for external dart_format process is dead. ExitCode: $exitCode")
            }

            val stdOut: String = withContext(Dispatchers.IO) { process.inputStream.readAllBytes().decodeToString() }
            val stdErr: String = withContext(Dispatchers.IO) { process.errorStream.readAllBytes().decodeToString() }
            Logger.logDebug(if (stdOut.isEmpty()) "StdOut: <empty>" else "StdOut:\n${stdOut.trim()}")
            Logger.logDebug(if (stdErr.isEmpty()) "StdErr: <empty>" else "StdErr:\n${stdErr.trim()}")
            val dartExecutable = if (OsTools.instance.isWindows) "dart.bat" else "dart"
            val stdErrContainsDartExecutable = stdErr.contains(dartExecutable)

            if (processWasAlive && exitCode == 0)
            {
                if (Constants.LOG_VERBOSE) Logger.logVerbose("$actionIngUpper external dart_format: Process finished.")
            }
            else if (stdErrContainsDartExecutable && (OsTools.instance.isWindows && exitCode == 1) || (!OsTools.instance.isWindows && exitCode == 127))
            {
                // Windows: 1: 'dart.bat' is not recognized as an internal or external command, operable program or batch file.
                // Linux: 127: /bin/bash: line 1: dart: command not found
                Logger.logDebug("$actionIngUpper external dart_format: Process finished. Exit code: $exitCode = Not found")

                val title = "Failed to $actionLower external dart_format. Dart executable not found. ExitCode: $exitCode"
                val content = "Could not find the Dart executable \"" + dartExecutable + "\".\n" +
                    "Please make sure that Dart is installed and callable from the commandline.\n" +
                    " \n" + // The space is necessary to force an empty line.
                    "If you installed Dart via Flutter then see the instructions below."

                NotificationTools.notifyError(
                    NotificationInfo(
                        content = content,
                        links = NotificationTools.createUpdateFlutterPathLinks(),
                        origin = null,
                        project = null,
                        title = title,
                        virtualFile = null
                    )
                )

                return false
            }
            else
            {
                Logger.logDebug("$actionIngUpper external dart_format: Process finished. Exit code: $exitCode")

                val processState = if (processWasAlive) "Abnormal exit" else "Dead process"
                val title = "Failed to $actionLower external dart_format. $processState. ExitCode: $exitCode"
                val content = "You can try to $actionLower it manually.\n" +
                    "Basically just execute this:<pre>dart pub global activate dart_format</pre>"
                val reportContent =
                    (if (stdOut.isEmpty()) "StdOut: <empty>" else "StdOut:\n${stdOut.trim()}") + "\n\n" +
                        (if (stdErr.isEmpty()) "StdErr: <empty>" else "StdErr:\n${stdErr.trim()}")
                val checkInstallationInstructionsLink = NotificationTools.createCheckInstallationInstructionsLink()
                val reportErrorLink = NotificationTools.createReportErrorLink(
                    content = reportContent,
                    gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                    origin = null,
                    stackTrace = null,
                    title = title
                )

                NotificationTools.notifyError(
                    NotificationInfo(
                        content = content,
                        links = listOf(checkInstallationInstructionsLink, reportErrorLink),
                        origin = null,
                        project = null,
                        title = title,
                        virtualFile = null
                    )
                )

                return false
            }

            NotificationTools.notifyInfo(
                NotificationInfo(
                    content = null,
                    links = null,
                    origin = null,
                    project = null,
                    title = "Successfully $actionEdLower external dart_format.",
                    virtualFile = null
                )
            )

            return true
        }
    }
}
