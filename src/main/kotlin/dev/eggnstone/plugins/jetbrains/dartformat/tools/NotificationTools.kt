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

        fun reportThrowable(throwable: Throwable, project: Project, fileName: String?, origin: String)
        {
            if (throwable is DartFormatException && throwable.type == FailType.Warning)
            {
                val optionalLocation = if (throwable.line != null && throwable.column != null) "Line ${throwable.line}, Column ${throwable.column}: " else ""
                val text = optionalLocation + throwable.message
                notifyWarning(text, NotificationInfo(project = project, fileName = fileName, origin = origin))
                return
            }

            val message = if (throwable.message == null) "Unknown error" else throwable.message!!
            if (DEBUG_NOTIFICATION_TOOLS) Logger.logError("throwable.message: $message")

            var stacktrace: String? = null
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

            var cleanedMessage = message
            cleanedMessage = cleanedMessage.replace("||", "|")
            cleanedMessage = cleanedMessage.replace("\r", "|")
            cleanedMessage = cleanedMessage.replace("||", "|")
            cleanedMessage = cleanedMessage.trim()
            if (cleanedMessage.endsWith("|"))
                cleanedMessage = cleanedMessage.substring(0, cleanedMessage.length - 1)

            val posPipe = cleanedMessage.indexOf("|")
            val title = if (posPipe == -1) cleanedMessage else cleanedMessage.substring(0, posPipe)
            val subTitleWithPipes: String? = if (posPipe == -1) null else cleanedMessage.substring(posPipe + 1)
            val subTitleWithNewLines = subTitleWithPipes?.replace("|", "\n")
            val subTitleWithHtmlBreaks = subTitleWithNewLines?.replace("\n", "<br/>")

            var body = "Please supply any additional information here, e.g. the source code that caused the error:\n\n"
            if (subTitleWithNewLines != null)
                body += "```\n$subTitleWithNewLines\n```\n"
            if (stacktrace != null)
                body += "```\n$stacktrace\n```\n"
            body += "origin: $origin\n"

            val githubRepo = if (throwable is DartFormatException && throwable.source == ExceptionSourceType.Remote) "dart_format" else "DartFormatJetbrainsPlugin"
            var text = "You found an error. Please report it.<br/>&quot;$title&quot;"
            if (subTitleWithHtmlBreaks != null)
                text += "<br/>$subTitleWithHtmlBreaks"

            val linkTitle = "Report error"
            val linkUrl = "https://github.com/eggnstone/$githubRepo/issues/new?title=${urlEncode(title)}&body=${urlEncode(body)}"

            notifyError(text, NotificationInfo(project = project, linkTitle = linkTitle, linkUrl = linkUrl, fileName = fileName, origin = origin))
        }

        fun notifyInfo(text: String, project: Project)
        {
            val textForLog = StringTools.toPipedText(text)
            Logger.logInfo("Info-Notification: $textForLog")
            notifyByToolWindowBalloon(text, NotificationType.INFORMATION, NotificationInfo(project = project))
        }

        fun notifyWarning(text: String, notificationInfo: NotificationInfo)
        {
            val textForLog = StringTools.toPipedText(text)
            Logger.logWarning("Warning-Notification: $textForLog")
            notifyByToolWindowBalloon(text, NotificationType.WARNING, notificationInfo)
        }

        fun notifyError(text: String, notificationInfo: NotificationInfo)
        {
            val textForLog = StringTools.toPipedText(text)
            Logger.logError("Error-Notification: $textForLog")
            notifyByToolWindowBalloon(text, NotificationType.ERROR, notificationInfo)
        }

        private fun notifyByToolWindowBalloon(text: String, type: NotificationType, notificationInfo: NotificationInfo)
        {
            val finalProject = notificationInfo.project ?: ProjectManager.getInstance().defaultProject
            var finalText = text.replace("\n", "<br/>")
            if (notificationInfo.fileName != null || notificationInfo.origin != null)
                finalText += "<br/>"
            if (notificationInfo.fileName != null)
                finalText += "<br/>" + notificationInfo.fileName
            if (notificationInfo.origin != null)
                finalText += "<br/>Origin: " + notificationInfo.origin

            val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("DartFormat")
            val notification: Notification = notificationGroup.createNotification("DartFormat", finalText, type)
            notification.subtitle = notificationInfo.subtitle

            if (notificationInfo.linkTitle != null && notificationInfo.linkUrl != null)
            {
                val action = NotificationAction.createSimple(notificationInfo.linkTitle) {
                    BrowserUtil.browse(notificationInfo.linkUrl)
                }
                notification.addAction(action)
            }

            notification.notify(finalProject)
        }
    }
}
