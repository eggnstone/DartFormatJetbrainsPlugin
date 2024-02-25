package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.ExceptionSourceType
import dev.eggnstone.plugins.jetbrains.dartformat.FailType
import dev.eggnstone.plugins.jetbrains.dartformat.data.LinkInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import org.intellij.markdown.html.urlEncode

class NotificationTools
{
    companion object
    {
        fun reportThrowable(
            fileName: String?,
            origin: String,
            project: Project?,
            throwable: Throwable
        )
        {
            if (throwable is DartFormatException && throwable.type == FailType.Warning)
            {
                val optionalLocation = if (throwable.line != null && throwable.column != null) "Line ${throwable.line}, Column ${throwable.column}: " else ""
                val title = optionalLocation + throwable.message
                notifyWarning(NotificationInfo(
                    content = null,
                    fileName = fileName,
                    links = null,
                    origin = origin,
                    project = project,
                    title = title
                ))
                return
            }

            val message = if (throwable.message == null) "Unknown error" else throwable.message!!
            if (Constants.DEBUG_NOTIFICATION_TOOLS) Logger.logError("throwable.message: $message")

            var stackTrace: String? = null
            if (throwable !is DartFormatException || throwable.source == ExceptionSourceType.Local)
            {
                stackTrace = throwable.stackTraceToString()
                var pos = stackTrace.lastIndexOf("dev.eggnstone")
                if (pos >= 0)
                {
                    pos = stackTrace.indexOf("\n", pos)
                    if (pos >= 0)
                        stackTrace = stackTrace.substring(0, pos - 1)
                }
            }

            val posPipe = message.indexOf("|")
            val title = if (posPipe == -1) message else message.substring(0, posPipe)
            var content: String? = null
            if (posPipe >= 0)
                content = message.substring(posPipe + 1).replace("|", "\n")

            val gitHubRepo = if (throwable is DartFormatException && throwable.source == ExceptionSourceType.Remote) "dart_format" else "DartFormatJetbrainsPlugin"
            val reportErrorLink = createReportErrorLink(
                content = content,
                gitHubRepo = gitHubRepo,
                origin = origin,
                stackTrace = stackTrace,
                title = title
            )
            notifyError(NotificationInfo(
                content = content,
                fileName = fileName,
                links = listOf(reportErrorLink),
                origin = origin,
                project = project,
                title = title
            ))
        }

        fun createReportErrorLink(
            content: String?,
            gitHubRepo: String,
            origin: String?,
            stackTrace: String?,
            title: String
        ): LinkInfo
        {
            var body = "Please supply any additional information here, e.g. the source code that caused the error:\n\n"

            if (content != null)
                body += "```\n$content\n```\n"

            if (stackTrace != null)
                body += "```\n$stackTrace\n```\n"

            if (origin != null)
                body += "Origin: $origin\n"

            body += "OS: ${System.getProperty("os.name")}\n"

            val linkName = "Report error"
            val linkUrl = "https://github.com/eggnstone/$gitHubRepo/issues/new?title=${urlEncode(title)}&body=${urlEncode(body)}"
            return LinkInfo(linkName, linkUrl)
        }

        fun notifyInfo(notificationInfo: NotificationInfo)
        {
            Logger.logInfo("Info-Notification: ${StringTools.toTextWithPipes(notificationInfo.title)}")
            if (notificationInfo.content != null)
                Logger.logInfo("                   ${StringTools.toTextWithPipes(notificationInfo.content)}")

            notifyByToolWindowBalloon(NotificationType.INFORMATION, notificationInfo)
        }

        fun notifyWarning(notificationInfo: NotificationInfo)
        {
            Logger.logWarning("Warning-Notification: ${StringTools.toTextWithPipes(notificationInfo.title)}")
            if (notificationInfo.content != null)
                Logger.logWarning("                      ${StringTools.toTextWithPipes(notificationInfo.content)}")

            notifyByToolWindowBalloon(NotificationType.WARNING, notificationInfo)
        }

        fun notifyError(notificationInfo: NotificationInfo)
        {
            Logger.logError("Error-Notification: ${StringTools.toTextWithPipes(notificationInfo.title)}")
            if (notificationInfo.content != null)
                Logger.logError("                    ${StringTools.toTextWithPipes(notificationInfo.content)}")

            notifyByToolWindowBalloon(NotificationType.ERROR, notificationInfo)
        }

        private fun notifyByToolWindowBalloon(type: NotificationType, notificationInfo: NotificationInfo)
        {
            val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("DartFormat")
            var content = StringTools.toTextWithHtmlBreaks(notificationInfo.title)
            if (notificationInfo.content != null)
            {
                if (!notificationInfo.content.startsWith("<pre>"))
                    content += "<br/><br/>"

                content += StringTools.toTextWithHtmlBreaks(notificationInfo.content)
            }

            if (notificationInfo.fileName != null || notificationInfo.origin != null)
            {
                content += "<br/>"

                if (notificationInfo.fileName != null)
                    content += "<br/>File: " + notificationInfo.fileName

                if (notificationInfo.origin != null)
                    content += "<br/>Origin: " + notificationInfo.origin
            }

            val notification: Notification = notificationGroup.createNotification(
                title = "DartFormat",
                content = content,
                type = type)

            if (notificationInfo.links != null)
                for (link in notificationInfo.links)
                {
                    val action = NotificationAction.createSimple(link.name) {
                        BrowserUtil.browse(link.url)
                    }
                    notification.addAction(action)
                }

            val finalProject = notificationInfo.project ?: ProjectManager.getInstance().defaultProject
            notification.notify(finalProject)
        }

        fun createCheckInstallationInstructionsLink(): LinkInfo //
            = LinkInfo("Installation instructions for dart_format", "https://pub.dev/packages/dart_format/install")
    }
}
