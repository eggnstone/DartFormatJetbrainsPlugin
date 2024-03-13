package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.enums.ExceptionSourceType
import dev.eggnstone.plugins.jetbrains.dartformat.enums.FailType
import dev.eggnstone.plugins.jetbrains.dartformat.data.LinkInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.plugin.ExternalDartFormat
import org.intellij.markdown.html.urlEncode

class NotificationTools
{
    companion object
    {
        fun reportThrowable(
            origin: String,
            project: Project?,
            throwable: Throwable,
            virtualFile: VirtualFile?
        )
        {
            val throwableLine = if (throwable is DartFormatException) throwable.line else null
            val throwableColumn = if (throwable is DartFormatException) throwable.column else null
            if (throwable is DartFormatException && throwable.type == FailType.Warning)
            {
                notifyWarning(NotificationInfo(
                    content = null,
                    links = null,
                    origin = origin,
                    project = project,
                    title = throwable.message,
                    virtualFile = virtualFile
                ), throwableLine, throwableColumn)
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
                links = listOf(reportErrorLink),
                origin = origin,
                project = project,
                title = title,
                virtualFile = virtualFile
            ), throwableLine, throwableColumn)
        }

        fun createCheckInstallationInstructionsLink(): LinkInfo //
            = LinkInfo("Installation instructions for dart_format", "https://pub.dev/packages/dart_format/install")

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
            body += "Plugin version: ${VersionTools.getVersion()}\n"
            body += "External dart_format version: ${ExternalDartFormat.instance.currentVersionText}\n"

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

        fun notifyWarning(notificationInfo: NotificationInfo, line: Int? = null, column: Int? = null)
        {
            Logger.logWarning("Warning-Notification: ${StringTools.toTextWithPipes(notificationInfo.title)}")
            if (notificationInfo.content != null)
                Logger.logWarning("                      ${StringTools.toTextWithPipes(notificationInfo.content)}")

            notifyByToolWindowBalloon(NotificationType.WARNING, notificationInfo, line, column)
        }

        fun notifyError(notificationInfo: NotificationInfo, line: Int? = null, column: Int? = null)
        {
            Logger.logError("Error-Notification: ${StringTools.toTextWithPipes(notificationInfo.title)}")
            if (notificationInfo.content != null)
                Logger.logError("                    ${StringTools.toTextWithPipes(notificationInfo.content)}")

            notifyByToolWindowBalloon(NotificationType.ERROR, notificationInfo, line, column)
        }

        private fun getShortFilePath(virtualFile: VirtualFile, project: Project?): String
        {
            if (project == null || project.basePath == null || !virtualFile.path.startsWith(project.basePath!!))
                return virtualFile.path

            return virtualFile.path.substring(project.basePath!!.length)
        }

        private fun notifyByToolWindowBalloon(type: NotificationType, notificationInfo: NotificationInfo, line: Int? = null, column: Int? = null)
        {
            var actionForNotification: NotificationAction? = null

            var locationForNotification: String? = null
            if (notificationInfo.virtualFile != null)
                locationForNotification = getShortFilePath(notificationInfo.virtualFile, notificationInfo.project)

            if (notificationInfo.project != null && notificationInfo.virtualFile != null)
            {
                val shortFileName = getShortFilePath(notificationInfo.virtualFile, notificationInfo.project)
                locationForNotification = "Line ${line}, Column $column in $shortFileName"

                val typeName = if (type == NotificationType.WARNING) "warning" else "error"
                actionForNotification = NotificationAction.createSimple("Open $typeName location") {
                    val openFileDescriptor = OpenFileDescriptor(
                        notificationInfo.project,
                        notificationInfo.virtualFile,
                        if (line == null) -1 else line - 1,
                        if (column == null) -1 else column - 1
                    )

                    // focusEditor = true
                    FileEditorManager.getInstance(notificationInfo.project).openFileEditor(openFileDescriptor, true)
                }
            }

            val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("DartFormat")
            var content = StringTools.toTextWithHtmlBreaks(notificationInfo.title)
            if (notificationInfo.content != null)
            {
                if (!notificationInfo.content.startsWith("<pre>"))
                    content += "<br/><br/>"

                content += StringTools.toTextWithHtmlBreaks(notificationInfo.content)
            }

            if (locationForNotification != null || notificationInfo.origin != null)
            {
                content += "<br/>"

                if (locationForNotification != null)
                    content += "<br/>Location: $locationForNotification"

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

            if (actionForNotification != null)
                notification.addAction(actionForNotification)

            val finalProject = notificationInfo.project ?: ProjectManager.getInstance().defaultProject
            notification.notify(finalProject)
        }
    }
}
