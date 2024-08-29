package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfigGetter
import dev.eggnstone.plugins.jetbrains.dartformat.data.FormatOrReportResult
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.enums.ExternalDartFormatState
import dev.eggnstone.plugins.jetbrains.dartformat.enums.FormatResultType
import dev.eggnstone.plugins.jetbrains.dartformat.enums.ResultType
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.PluginTools
import java.util.*

class FormatAction : AnAction()
{
    companion object
    {
        const val CLASS_NAME = "FormatAction"
    }

    init
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("FormatAction: init")
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        val methodName = "$CLASS_NAME.actionPerformed"

        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        var lastVirtualFile: VirtualFile? = null

        val config = DartFormatConfigGetter.get()

        if (config.hasNothingEnabled())
        {
            val title = "No formatting options enabled."
            val openSettingsLink = NotificationTools.createOpenSettingsLink()
            NotificationTools.notifyWarning(
                NotificationInfo(
                    content = null,
                    links = listOf(openSettingsLink),
                    origin = "$methodName/1",
                    project = project,
                    title = title,
                    virtualFile = null
                )
            )
            return
        }

        try
        {
            val startTime = Date()

            val finalVirtualFiles = mutableSetOf<VirtualFile>()
            val collectVirtualFilesIterator = CollectVirtualFilesIterator(finalVirtualFiles)
            val selectedVirtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)

            if (selectedVirtualFiles == null)
            {
                if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("No files selected.")
            }
            else
            {
                if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("${selectedVirtualFiles.size} selected files:")
                for (selectedVirtualFile in selectedVirtualFiles)
                {
                    if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("  Selected file: $selectedVirtualFile")
                    VfsUtilCore.iterateChildrenRecursively(selectedVirtualFile, this::filterDartFiles, collectVirtualFilesIterator)
                }
            }

            var changedCount = 0
            var warningCount = 0
            var errorCount = 0
            if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("${finalVirtualFiles.size} final files:")
            CommandProcessor.getInstance().runUndoTransparentAction {
                for (finalVirtualFile in finalVirtualFiles)
                {
                    if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("  Final file: $finalVirtualFile")
                    lastVirtualFile = finalVirtualFile
                    val startTime2 = Date()
                    val result = formatDartFile(finalVirtualFile, project, warningCount == 0)
                    val endTime2 = Date()
                    val diffTime2 = endTime2.time - startTime2.time
                    val seconds2 = diffTime2 / 1000.0
                    if (Constants.SHOW_SLOW_TIMINGS && seconds2 >= 5.0)
                        NotificationTools.notifyWarning(
                            NotificationInfo(
                                content = null,
                                links = null,
                                origin = "$methodName/2",
                                project = project,
                                title = "Took ${seconds2}s to format $finalVirtualFile.",
                                virtualFile = null
                            )
                        )

                    if (result == FormatResultType.Error)
                    {
                        errorCount++
                        break
                    }

                    if (result == FormatResultType.Warning)
                        warningCount++

                    if (result == FormatResultType.SomethingChanged)
                        changedCount++
                }
            }

            if (errorCount == 0 || Constants.SHOW_TIMINGS_EVEN_AFTER_ERROR)
            {
                val endTime = Date()
                val diffTime = endTime.time - startTime.time
                val diffTimeText = if (diffTime < 1000) "$diffTime ms" else "${diffTime / 1000.0} s"

                var finalVirtualFilesText = "${finalVirtualFiles.size} file"
                if (finalVirtualFiles.size != 1)
                    finalVirtualFilesText += "s"

                val changedFilesText: String = when (changedCount)
                {
                    0 -> "Nothing"
                    1 -> "1 file"
                    else -> "$changedCount files"
                }

                val warningsText: String? = when (warningCount)
                {
                    0 -> null
                    1 -> "1 warning"
                    else -> "$warningCount warnings"
                }

                var title = "Formatting $finalVirtualFilesText took $diffTimeText."

                if (errorCount > 0)
                    title += "\nAn error occurred." //+ errorCount

                if (warningsText != null)
                    title += "\n$warningsText encountered."

                title += "\n$changedFilesText changed."
                NotificationTools.notifyInfo(
                    NotificationInfo(
                        content = null,
                        links = null,
                        origin = "$methodName/3",
                        project = project,
                        title = title,
                        virtualFile = null
                    )
                )
            }
        }
        catch (e: Exception)
        {
            NotificationTools.reportThrowable(
                origin = "$methodName/4",
                project = project,
                throwable = e,
                virtualFile = lastVirtualFile
            )
        }
        catch (e: Error)
        {
            // catch errors, too, in order to report all problems, e.g.:
            // - java.lang.AssertionError: Wrong line separators: '...\r\n...'
            NotificationTools.reportThrowable(
                origin = "$methodName/5",
                project = project,
                throwable = e,
                virtualFile = lastVirtualFile
            )
        }
    }

    private fun filterDartFiles(virtualFile: VirtualFile): Boolean = virtualFile.isDirectory || PluginTools.isDartFile(virtualFile)

    private fun formatDartFile(virtualFile: VirtualFile, project: Project, notifyWarnings: Boolean): FormatResultType
    {
        try
        {
            val fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(virtualFile)
            return if (fileEditor == null)
                formatDartFileByBinaryContent(project, virtualFile, notifyWarnings)
            else
                formatDartFileByFileEditor(project, fileEditor, notifyWarnings)
        }
        catch (e: DartFormatException)
        {
            throw e
        }
        catch (e: Exception)
        {
            throw DartFormatException.localError("${virtualFile.path}\n${e.message}", e)
        }
        catch (e: Error)
        {
            // necessary?
            throw DartFormatException.localError("${virtualFile.path}\n${e.message}", e)
        }
    }

    private fun formatDartFileByBinaryContent(project: Project, virtualFile: VirtualFile, notifyWarnings: Boolean): FormatResultType
    {
        if (!virtualFile.isWritable)
        {
            if (Constants.DEBUG_FORMAT_ACTION)
            {
                Logger.logDebug("formatDartFileByBinaryContent: $virtualFile")
                Logger.logDebug("  !virtualFile.isWritable")
            }

            return FormatResultType.NothingChanged
        }

        val inputBytes = virtualFile.inputStream.readAllBytes()
        val inputText = String(inputBytes)
        val formatOrReportResult = formatOrReport(project, inputText, virtualFile, notifyWarnings)
        val formatResultText = formatOrReportResult.text
        @Suppress("FoldInitializerAndIfToElvis", "RedundantSuppression", "DuplicatedCode")
        if (formatResultText == null)
            return if (formatOrReportResult.hasWarning) FormatResultType.Warning else FormatResultType.Error

        if (formatResultText == inputText)
        {
            if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("Nothing changed.")
            return FormatResultType.NothingChanged
        }

        val outputBytes = formatResultText.toByteArray()
        ApplicationManager.getApplication().runWriteAction {
            virtualFile.setBinaryContent(outputBytes)
        }

        if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("Something changed.")
        return FormatResultType.SomethingChanged
    }

    private fun formatDartFileByFileEditor(project: Project, fileEditor: FileEditor, notifyWarnings: Boolean): FormatResultType
    {
        if (fileEditor !is TextEditor)
        {
            if (Constants.DEBUG_FORMAT_ACTION)
            {
                Logger.logDebug("formatDartFileByFileEditor: $fileEditor")
                Logger.logDebug("  fileEditor !is TextEditor")
            }
            return FormatResultType.NothingChanged
        }

        val editor = fileEditor.editor

        val document = editor.document
        val inputText = document.text
        val formatOrReportResult = formatOrReport(project, inputText, fileEditor.file, notifyWarnings)
        val formatResultText = formatOrReportResult.text
        @Suppress("FoldInitializerAndIfToElvis", "RedundantSuppression")
        if (formatResultText == null)
            return if (formatOrReportResult.hasWarning) FormatResultType.Warning else FormatResultType.Error

        if (formatResultText == inputText)
        {
            if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("Nothing changed.")
            return FormatResultType.NothingChanged
        }

        val fixedOutputText: String = if (formatResultText.contains("\r\n"))
        {
            Logger.logDebug("#################################################")
            Logger.logDebug("Why does the outputText contain wrong linebreaks?")
            Logger.logDebug("#################################################")
            formatResultText.replace("\r\n", "\n")
        }
        else
            formatResultText

        ApplicationManager.getApplication().runWriteAction {
            document.setText(fixedOutputText)
        }

        if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("Something changed.")
        return FormatResultType.SomethingChanged
    }

    private fun formatOrReport(project: Project, inputText: String, virtualFile: VirtualFile, notifyWarnings: Boolean): FormatOrReportResult
    {
        val methodName = "$CLASS_NAME.formatOrReport"

        if (ExternalDartFormat.instance.state != ExternalDartFormatState.STARTED)
        {
            ExternalDartFormat.instance.notifyWhenReady = true
            var message = when (ExternalDartFormat.instance.state)
            {
                ExternalDartFormatState.FAILED_TO_INSTALL -> "failed to install"
                ExternalDartFormatState.FAILED_TO_START -> "failed to start"
                ExternalDartFormatState.INSTALLING -> "is still installing"
                ExternalDartFormatState.NOT_STARTED -> "hasn't started yet"
                ExternalDartFormatState.STARTED -> throw IllegalStateException("External dart_format should not be started.")
                ExternalDartFormatState.STARTING -> "is still starting"
                ExternalDartFormatState.STOPPED -> "has stopped"
                ExternalDartFormatState.STOPPING -> "is stopping"
                ExternalDartFormatState.UPDATING -> "is still updating"
            }

            message = if (ExternalDartFormat.instance.state == ExternalDartFormatState.INSTALLING
                || ExternalDartFormat.instance.state == ExternalDartFormatState.STARTING
                || ExternalDartFormat.instance.state == ExternalDartFormatState.UPDATING
            )
                "Cannot format yet because external dart_format $message."
            else
                "Cannot format because external dart_format $message."

            val hasFatalError = (ExternalDartFormat.instance.state == ExternalDartFormatState.FAILED_TO_INSTALL
                || ExternalDartFormat.instance.state == ExternalDartFormatState.FAILED_TO_START)

            val info = NotificationInfo(
                content = null,
                links = null,
                origin = "$methodName/1",
                project = project,
                title = message,
                virtualFile = null
            )

            if (hasFatalError)
                NotificationTools.notifyError(info)
            else
                NotificationTools.notifyInfo(info)

            return FormatOrReportResult(null, false, hasFatalError)
        }

        if (inputText.isEmpty())
            return FormatOrReportResult(inputText, hasWarning = false, hasFatalError = false)

        val formatResult = format(inputText, virtualFile, project)

        if (formatResult.resultType == ResultType.Error)
        {
            if (formatResult.throwable == null)
            {
                val reportErrorLink = NotificationTools.createReportErrorLink(
                    content = null,
                    gitHubRepo = Constants.REPO_NAME_DART_FORMAT,
                    origin = "$methodName/2",
                    stackTrace = null,
                    title = formatResult.text
                )
                NotificationTools.notifyError(
                    NotificationInfo(
                        content = null,
                        listOf(reportErrorLink),
                        origin = "$methodName/3",
                        project = project,
                        title = formatResult.text,
                        virtualFile = virtualFile
                    )
                )
            }
            else
                NotificationTools.reportThrowable(
                    origin = "$methodName/4",
                    project = project,
                    throwable = formatResult.throwable,
                    virtualFile = virtualFile
                )

            return FormatOrReportResult(null, hasWarning = false, hasFatalError = false)
        }

        if (formatResult.resultType == ResultType.Warning)
        {
            if (notifyWarnings)
                if (formatResult.throwable == null)
                    NotificationTools.notifyWarning(
                        NotificationInfo(
                            content = null,
                            links = null,
                            origin = "$methodName/5",
                            project = project,
                            title = formatResult.text,
                            virtualFile = virtualFile
                        )
                    )
                else
                    NotificationTools.reportThrowable(
                        origin = "$methodName/6",
                        project = project,
                        throwable = formatResult.throwable,
                        virtualFile = virtualFile
                    )

            return FormatOrReportResult(null, hasWarning = true, hasFatalError = false)
        }

        if (formatResult.text.isEmpty())
        {
            val title = "Result from external dart_format is empty."
            val reportErrorLink = NotificationTools.createReportErrorLink(
                content = null,
                gitHubRepo = Constants.REPO_NAME_DART_FORMAT,
                origin = "$methodName/7",
                stackTrace = null,
                title = title
            )
            NotificationTools.notifyError(
                NotificationInfo(
                    content = null,
                    listOf(reportErrorLink),
                    origin = "$methodName/8",
                    project = project,
                    title = title,
                    virtualFile = virtualFile
                )
            )

            return FormatOrReportResult(null, hasWarning = false, hasFatalError = false)
        }

        return FormatOrReportResult(formatResult.text, hasWarning = false, hasFatalError = false)
    }

    private fun format(inputText: String, virtualFile: VirtualFile, project: Project): FormatResult
    {
        if (inputText.isEmpty())
            return FormatResult.ok("")

        val config = DartFormatConfigGetter.get()
        val jsonConfig = config.toJson()
        return ExternalDartFormat.instance.formatViaChannel(inputText, jsonConfig, virtualFile, project)
    }
}
