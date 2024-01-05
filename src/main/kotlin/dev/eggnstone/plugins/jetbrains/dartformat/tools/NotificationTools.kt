package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.FailType

class NotificationTools
{
    companion object
    {
        private const val DEBUG_NOTIFICATION_TOOLS = false

        fun reportThrowable(throwable: Throwable, project: Project)
        {
            if (throwable is DartFormatException && throwable.type == FailType.WARNING)
            {
                val optionalLocation = if (throwable.line != null && throwable.column != null) "Line ${throwable.line}, Column ${throwable.column}: " else ""
                val text = optionalLocation + throwable.message
                notifyWarning(listOf(text), project)
                return
            }

            val message = if (throwable.message == null) "Unknown error" else throwable.message!!
            if (DEBUG_NOTIFICATION_TOOLS) Logger.logError("throwable.message: $message")

            var stacktrace = throwable.stackTraceToString()
            var pos = stacktrace.lastIndexOf("dev.eggnstone")
            if (pos >= 0)
            {
                pos = stacktrace.indexOf("\n", pos)
                if (pos >= 0)
                    stacktrace = stacktrace.substring(0, pos - 1)
            }

            val safeMessageForTitle = message.replace("\"", "&quot;").replace("\n", " ")
            val safeStacktraceForBody = stacktrace.replace("\"", "&quot;")
            val title = "Error while formatting: $safeMessageForTitle"
            val body = "Please supply any additional information here, e.g. the source code that cause the error:\n\n```\n$safeStacktraceForBody\n```"
            val url = "https://github.com/eggnstone/DartFormatJetbrainsPlugin/issues/new?title=$title&body=$body"
            val text = "You found an error. Please <a href=\"$url\">report</a> it.<br/>$message"

            notifyError(text, project)
        }

        fun notifyInfo(lines: List<String>, project: Project, subtitle: String? = null)
        {
            notifyByToolWindowBalloon(lines, NotificationType.INFORMATION, project, subtitle)
        }

        fun notifyWarning(lines: List<String>, project: Project, subtitle: String? = null)
        {
            notifyByToolWindowBalloon(lines, NotificationType.WARNING, project, subtitle)
        }

        //private fun notifyErrorWithNormalLineBreaks(lines: List<String>, project: Project, subtitle: String? = null)
        fun notifyError(text: String, project: Project, subtitle: String? = null)
        {
            notifyByToolWindowBalloon(text, NotificationType.ERROR, project, subtitle)
        }

        private fun notifyByToolWindowBalloon(lines: List<String>, type: NotificationType, project: Project, subtitle: String? = null)
        {
            val combinedLines = lines.joinToString("<br/>")
            notifyByToolWindowBalloon(combinedLines, type, project, subtitle)
        }

        private fun notifyByToolWindowBalloon(text: String, type: NotificationType, project: Project, subtitle: String? = null)
        {
            val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("DartFormat")
            val notification = notificationGroup.createNotification("DartFormat", text, type)
            notification.subtitle = subtitle
            /*
            val action = NotificationAction.createSimple("TODO XYZ") {
                BrowserUtil.browse(url)
            }
            notification.addAction(action)
            */
            notification.setListener(NotificationListener.UrlOpeningListener(true))
            notification.notify(project)
        }
    }
}
