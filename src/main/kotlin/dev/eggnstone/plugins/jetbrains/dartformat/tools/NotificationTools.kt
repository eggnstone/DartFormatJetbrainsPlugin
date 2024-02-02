package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.ExceptionSourceType
import dev.eggnstone.plugins.jetbrains.dartformat.FailType
import org.intellij.markdown.html.urlEncode

class NotificationTools
{
    companion object
    {
        private const val DEBUG_NOTIFICATION_TOOLS = false

        fun reportThrowable(throwable: Throwable, project: Project, fileName: String?, fileNameSource: String)
        {
            if (throwable is DartFormatException && throwable.type == FailType.Warning)
            {
                val optionalLocation = if (throwable.line != null && throwable.column != null) "Line ${throwable.line}, Column ${throwable.column}: " else ""
                val text = optionalLocation + throwable.message
                notifyWarning(text, project)
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
            val url = "https://github.com/eggnstone/$githubRepo/issues/new?title=${urlEncode(title)}&body=${urlEncode(body)}"
            val text = "You found an error." +
                " Please <a href=\"$url\">report</a> it." +
                "<br/>$title" +
                "<br/>$fileName/$fileNameSource"

            notifyError(text, project)
        }

        fun notifyInfo(lines: List<String>, project: Project, subtitle: String? = null)
        {
            Logger.log("Info-Notification: $lines")
            notifyByToolWindowBalloon(lines, NotificationType.INFORMATION, project, subtitle)
        }

        fun notifyWarning(text: String, project: Project, subtitle: String? = null)
        {
            Logger.log("Warning-Notification: $text")
            notifyByToolWindowBalloon(text, NotificationType.WARNING, project, subtitle)
        }

        fun notifyError(text: String, project: Project, subtitle: String? = null)
        {
            Logger.log("Error-Notification: $text")
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
            @Suppress("DEPRECATION")
            notification.setListener(NotificationListener.UrlOpeningListener(true))
            notification.notify(project)
        }
    }
}
