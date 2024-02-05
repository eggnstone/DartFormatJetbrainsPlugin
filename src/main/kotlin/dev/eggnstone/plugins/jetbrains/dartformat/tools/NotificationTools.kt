package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.ExceptionSourceType
import dev.eggnstone.plugins.jetbrains.dartformat.FailType
import org.intellij.markdown.html.urlEncode

class NotificationTools
{
    companion object
    {
        private const val DEBUG_NOTIFICATION_TOOLS = false

        fun reportThrowable(throwable: Throwable, project: Project, fileName: String?, source: String)
        {
            if (throwable is DartFormatException && throwable.type == FailType.Warning)
            {
                val optionalLocation = if (throwable.line != null && throwable.column != null) "Line ${throwable.line}, Column ${throwable.column}: " else ""
                val text = optionalLocation + throwable.message
                notifyWarning(text, NotificationInfo(project))
                return
            }

            val message = if (throwable.message == null) "Unknown error" else throwable.message!!
            if (DEBUG_NOTIFICATION_TOOLS) Logger.logError("throwable.message: $message")

            var stacktrace = ""
            if (throwable !is DartFormatException || throwable.source == ExceptionSourceType.Local)
            {
                stacktrace = throwable.stackTraceToString()
                var pos = stacktrace.lastIndexOf("dev.eggnstone")
                if (pos >= 0)
                {
                    pos = stacktrace.indexOf("\n", pos)
                    if (pos >= 0)
                        stacktrace = stacktrace.substring(0, pos - 1)
                }
            }

            val posPipe = message.indexOf("|")
            val title = if (posPipe == -1) message else message.substring(0, posPipe)
            val subTitle = if (posPipe == -1) "" else message.substring(posPipe + 1)

            var body = "Please supply any additional information here, e.g. the source code that caused the error:\n\n"
            if (subTitle.isNotEmpty())
                body += "```\n$subTitle\n```"
            if (stacktrace.isNotEmpty())
                body += "```\n$stacktrace\n```"

            val githubRepo = if (throwable is DartFormatException && throwable.source == ExceptionSourceType.Remote) "dart_format" else "DartFormatJetbrainsPlugin"
            val text = "You found an error." +
                " Please report it." +
                "<br/>$title" +
                "<br/>$fileName/$source"
            val linkTitle = "Report error"
            val linkUrl = "https://github.com/eggnstone/$githubRepo/issues/new?title=${urlEncode(title)}&body=${urlEncode(body)}"

            notifyError(text, NotificationInfo(project, linkTitle = linkTitle, linkUrl = linkUrl))
        }

        fun notifyInfo(lines: List<String>, project: Project)
        {
            Logger.log("Info-Notification: $lines")
            notifyByToolWindowBalloon(lines, NotificationType.INFORMATION, NotificationInfo(project))
        }

        // TODO: add source to notifyX() methods
        fun notifyWarning(text: String, notificationInfo: NotificationInfo? = null)
        {
            Logger.log("Warning-Notification: $text")
            notifyByToolWindowBalloon(text, NotificationType.WARNING, notificationInfo)
        }

        fun notifyError(text: String, notificationInfo: NotificationInfo? = null)
        {
            Logger.log("Error-Notification: $text")
            notifyByToolWindowBalloon(text, NotificationType.ERROR, notificationInfo)
        }

        @Suppress("SameParameterValue")
        private fun notifyByToolWindowBalloon(lines: List<String>, type: NotificationType, notificationInfo: NotificationInfo? = null)
        {
            val combinedLines = lines.joinToString("<br/>")
            notifyByToolWindowBalloon(combinedLines, type, notificationInfo)
        }

        private fun notifyByToolWindowBalloon(text: String, type: NotificationType, notificationInfo: NotificationInfo? = null)
        {
            val finalNotificationInfo = notificationInfo ?: NotificationInfo(ProjectManager.getInstance().defaultProject)
            val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("DartFormat")
            val notification: Notification = notificationGroup.createNotification("DartFormat", text, type)
            notification.subtitle = finalNotificationInfo.subtitle

            if (finalNotificationInfo.linkTitle != null && finalNotificationInfo.linkUrl != null)
            {
                val action = NotificationAction.createSimple(finalNotificationInfo.linkTitle) {
                    BrowserUtil.browse(finalNotificationInfo.linkUrl)
                }
                notification.addAction(action)
            }

            notification.notify(finalNotificationInfo.project)
        }
    }
}
