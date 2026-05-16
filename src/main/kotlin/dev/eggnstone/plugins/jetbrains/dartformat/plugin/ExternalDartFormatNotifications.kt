package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.StringTools

// Domain-specific notification builders for the dart_format startup flow.
// Kept here (next to ExternalDartFormat) rather than in the generic NotificationTools
// because the message text is tied to the dart_format install/upgrade story.
class ExternalDartFormatNotifications
{
    companion object
    {
        // Matches the (non-localized) English stderr emitted by Flutter's bin/internal bootstrap
        // scripts when it has to fetch a fresh Dart SDK before running anything. Substrings — not
        // exact lines — because the .sh variant interpolates "$OS $ARCH" into the message.
        // .bat / .sh / .ps1 all hardcode English via plain ECHO / echo / Write-Host.
        fun isFlutterSdkBootstrapStderr(s: String): Boolean =
            s.contains("Checking Dart SDK version") ||
                (s.contains("Downloading") && s.contains("Dart SDK"))

        fun notifyFlutterSdkBootstrapInProgress()
        {
            NotificationTools.notifyInfo(
                NotificationInfo(
                    content = "This usually takes under a minute. dart_format will start automatically once Flutter finishes.",
                    links = null,
                    origin = null,
                    project = null,
                    title = "Flutter is updating its bundled Dart SDK before launching dart_format ...",
                    virtualFile = null
                )
            )
        }

        fun notifyExpectedJsonButReceivedPlainText(
            jsonEncodedResponse: String,
            stdOutLines: String?,
            stdErrLines: String?,
            inputStreamReader: StreamReader,
            errorStreamReader: StreamReader
        )
        {
            val title = "External dart_format: Expected connection details in JSON but received plain text: " +
                StringTools.toDisplayString(jsonEncodedResponse, 200)

            var content = ""
            if (stdOutLines != null)
                content += "\nStdOut1:\n$stdOutLines"
            content += TimedReader.receiveLines(inputStreamReader, "\nStdOut2:\n") ?: ""
            if (stdErrLines != null)
                content += "\nStdErr1:\n$stdErrLines"
            content += TimedReader.receiveLines(errorStreamReader, "\nStdErr2:\n") ?: ""
            content = content.trim()

            if (content.isNotEmpty())
                content += "\n"

            content += if (isFlutterSdkBootstrapStderr(content))
                "Flutter is downloading a Dart SDK before dart_format can start. Wait for that to finish, then retry."
            else
                "Did you install the dart_format package?\n" +
                    "Basically just execute this:<pre>dart pub global activate dart_format</pre>"

            val checkInstallationInstructionsLink = NotificationTools.createCheckInstallationInstructionsLink()
            val reportErrorLink = NotificationTools.createReportErrorLink(
                content = content.ifEmpty { null },
                gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                origin = null,
                stackTrace = null,
                title = title
            )

            NotificationTools.notifyError(
                NotificationInfo(
                    content = content.ifEmpty { null },
                    links = listOf(checkInstallationInstructionsLink, reportErrorLink),
                    origin = null,
                    project = null,
                    title = title,
                    virtualFile = null
                )
            )
        }

        fun notifyFailedToFind(error: DartFormatException)
        {
            val errorMessage = error.message
            val title = "Failed to find external dart_format: $errorMessage"
            val content = "Did you install the dart_format package?\n" +
                "Basically just execute this:<pre>dart pub global activate dart_format</pre>"
            val checkInstallationInstructionsLink = NotificationTools.createCheckInstallationInstructionsLink()
            val reportErrorLink = NotificationTools.createReportErrorLink(
                content = null,
                gitHubRepo = Constants.REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN,
                origin = null,
                stackTrace = null,
                title = title
            )

            // No point asking the user to file a bug when the executable simply isn't installed yet.
            val showReportErrorLink = !errorMessage.contains("Cannot find the dart_format package: File does not exist at expected location:")
            val links = if (showReportErrorLink) listOf(checkInstallationInstructionsLink, reportErrorLink) else listOf(checkInstallationInstructionsLink)

            NotificationTools.notifyError(
                NotificationInfo(
                    content = content,
                    links = links,
                    origin = null,
                    project = null,
                    title = title,
                    virtualFile = null
                )
            )
        }
    }
}
