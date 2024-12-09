package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatPersistentStateConfigurable
import dev.eggnstone.plugins.jetbrains.dartformat.data.LinkInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.enums.ExceptionSourceType
import dev.eggnstone.plugins.jetbrains.dartformat.enums.FailType
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
            val throwableLine = if (throwable is DartFormatException && throwable.line != null) throwable.line else 0
            val throwableColumn = if (throwable is DartFormatException && throwable.column != null) throwable.column else 0
            if (throwable is DartFormatException && throwable.type == FailType.Warning)
            {
                notifyWarning(
                    NotificationInfo(
                        content = null,
                        links = null,
                        origin = origin,
                        project = project,
                        title = throwable.message,
                        virtualFile = virtualFile
                    ), throwableLine, throwableColumn
                )
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

            val gitHubRepo = if (throwable is DartFormatException && throwable.source == ExceptionSourceType.Remote) "dart_format" else "DartFormatJetBrainsPlugin"
            val reportErrorLink = createReportErrorLink(
                content = content,
                gitHubRepo = gitHubRepo,
                origin = origin,
                stackTrace = stackTrace,
                title = title
            )
            notifyError(
                NotificationInfo(
                    content = content,
                    links = listOf(reportErrorLink),
                    origin = origin,
                    project = project,
                    title = title,
                    virtualFile = virtualFile
                ), throwableLine, throwableColumn
            )
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
            {
                val shortStackTrace = if (stackTrace.length <= Constants.MAX_STACK_TRACE_LENGTH) stackTrace else stackTrace.substring(0, Constants.MAX_STACK_TRACE_LENGTH - 3) + "..."
                body += "```\n$shortStackTrace\n```\n"
            }

            body += "```"
            if (origin != null)
                body += "Origin: $origin\n"
            body += "OS: ${System.getProperty("os.name")}\n"
            body += "Plugin version: ${VersionTools.getVersion()}\n"
            body += "External dart_format version: ${ExternalDartFormat.instance.currentVersionText}\n"
            body += "```"

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

        fun notifyWarning(notificationInfo: NotificationInfo, line: Int = 0, column: Int = 0)
        {
            Logger.logWarning("Warning-Notification: ${StringTools.toTextWithPipes(notificationInfo.title)}")
            if (notificationInfo.content != null)
                Logger.logWarning("                      ${StringTools.toTextWithPipes(notificationInfo.content)}")

            notifyByToolWindowBalloon(NotificationType.WARNING, notificationInfo, line, column)
        }

        fun notifyError(notificationInfo: NotificationInfo, line: Int = 0, column: Int = 0)
        {
            Logger.logError("Error-Notification: ${StringTools.toTextWithPipes(notificationInfo.title)}")
            if (notificationInfo.content != null)
                Logger.logError("                    ${StringTools.toTextWithPipes(notificationInfo.content)}")

            notifyByToolWindowBalloon(NotificationType.ERROR, notificationInfo, line, column)
        }

        internal fun getShortFilePath(virtualFile: VirtualFile, project: Project?): String
        {
            if (project == null || project.basePath == null || !virtualFile.path.startsWith(project.basePath!!))
                return virtualFile.path

            return virtualFile.path.substring(project.basePath!!.length)
        }

        private fun notifyByToolWindowBalloon(type: NotificationType, notificationInfo: NotificationInfo, line: Int = 0, column: Int = 0)
        {
            var actionForNotification: NotificationAction? = null

            var locationForNotification: String? = null
            if (notificationInfo.virtualFile != null)
                locationForNotification = getShortFilePath(notificationInfo.virtualFile, notificationInfo.project)

            if (notificationInfo.project != null && notificationInfo.virtualFile != null)
            {
                val shortFileName = getShortFilePath(notificationInfo.virtualFile, notificationInfo.project)

                val openFileDescriptor: OpenFileDescriptor
                if (line > 0 && column > 0)
                {
                    locationForNotification = "Line ${line}, Column $column in $shortFileName"
                    openFileDescriptor = OpenFileDescriptor(
                        notificationInfo.project,
                        notificationInfo.virtualFile,
                        line - 1,
                        column - 1
                    )
                }
                else
                {
                    locationForNotification = shortFileName
                    openFileDescriptor = OpenFileDescriptor(
                        notificationInfo.project,
                        notificationInfo.virtualFile
                    )
                }

                val typeName = if (type == NotificationType.WARNING) "warning" else "error"
                actionForNotification = NotificationAction.createSimple("Open $typeName location") {
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

                if (Constants.DEBUG && notificationInfo.origin != null)
                    content += "<br/>Origin: " + notificationInfo.origin
            }

            val notification: Notification = notificationGroup.createNotification(
                title = "DartFormat",
                content = content,
                type = type
            )

            if (notificationInfo.links != null)
                for (link in notificationInfo.links)
                {
                    val runnable: Runnable = when (link.url)
                    {
                        "action://openSettings" -> Runnable {
                            val finalProject = notificationInfo.project ?: ProjectManager.getInstance().defaultProject
                            ShowSettingsUtil.getInstance().showSettingsDialog(
                                finalProject,
                                DartFormatPersistentStateConfigurable::class.java
                            )
                        }
                        else -> Runnable { BrowserUtil.browse(link.url) }
                    }

                    val safeLinkName = link.name.replace("_", "&_")
                    val action = NotificationAction.createSimple(safeLinkName, runnable)
                    notification.addAction(action)
                }

            if (actionForNotification != null)
                notification.addAction(actionForNotification)

            val finalProject = notificationInfo.project ?: ProjectManager.getInstance().defaultProject
            notification.notify(finalProject)
        }

        fun createOpenSettingsLink(): LinkInfo
        {
            return LinkInfo("Open DartFormat settings", "action://openSettings")
        }
    }
}
