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
import dev.eggnstone.plugins.jetbrains.dartformat.enums.ResultType
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatPersistentStateComponent
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.enums.FormatResultType
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
        Logger.logDebug("FormatAction: init")
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        val methodName = "$CLASS_NAME.actionPerformed"

        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        var lastVirtualFile: VirtualFile? = null

        val config = getConfig()

        if (config.hasNothingEnabled())
        {
            val title = "No formatting option enabled"
            val content = "Please enable your desired formatting options:" +
                "<pre>File -&gt; Settings -&gt; Other Settings -&gt; DartFormat</pre>"
            NotificationTools.notifyWarning(NotificationInfo(
                content = content,
                links = null,
                origin = "$methodName/1",
                project = project,
                title = title,
                virtualFile = null
            ))
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

            var changedFiles = 0
            var encounteredError = false
            if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("${finalVirtualFiles.size} final files:")
            CommandProcessor.getInstance().runUndoTransparentAction {
                for (finalVirtualFile in finalVirtualFiles)
                {
                    if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("  Final file: $finalVirtualFile")
                    lastVirtualFile = finalVirtualFile
                    val startTime2 = Date()
                    val result = formatDartFile(finalVirtualFile, project)
                    val endTime2 = Date()
                    val diffTime2 = endTime2.time - startTime2.time

                    val seconds2 = diffTime2 / 1000.0
                    if (Constants.SHOW_SLOW_TIMINGS && seconds2 >= 5.0)
                        NotificationTools.notifyWarning(NotificationInfo(
                            content = null,
                            links = null,
                            origin = null,
                            project = project,
                            title = "Took ${seconds2}s to format $finalVirtualFile.",
                            virtualFile = null
                        ))

                    if (result == FormatResultType.Error)
                    {
                        encounteredError = true
                        if (Constants.CANCEL_PROCESSING_ON_ERROR)
                            break
                    }

                    if (result == FormatResultType.SomethingChanged)
                        changedFiles++
                }
            }

            if (!encounteredError || Constants.SHOW_TIMINGS_EVEN_AFTER_ERROR)
            {
                val endTime = Date()
                val diffTime = endTime.time - startTime.time
                val diffTimeText = if (diffTime < 1000) "$diffTime ms" else "${diffTime / 1000.0} s"

                var finalVirtualFilesText = "${finalVirtualFiles.size} file"
                if (finalVirtualFiles.size != 1)
                    finalVirtualFilesText += "s"

                val changedFilesText: String = when (changedFiles)
                {
                    0 -> "Nothing"
                    1 -> "1 file"
                    else -> "$changedFiles files"
                }

                val title = "Formatting $finalVirtualFilesText took $diffTimeText.\n$changedFilesText changed."
                NotificationTools.notifyInfo(NotificationInfo(
                    content = null,
                    links = null,
                    origin = null,
                    project = project,
                    title = title,
                    virtualFile = null
                ))
            }
        }
        catch (e: Exception)
        {
            NotificationTools.reportThrowable(
                origin = "$methodName/3",
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
                origin = "$methodName/4",
                project = project,
                throwable = e,
                virtualFile = lastVirtualFile
            )
        }
    }

    private fun filterDartFiles(virtualFile: VirtualFile): Boolean = virtualFile.isDirectory || PluginTools.isDartFile(virtualFile)

    private fun formatDartFile(virtualFile: VirtualFile, project: Project): FormatResultType
    {
        try
        {
            val fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(virtualFile)
            return if (fileEditor == null)
                formatDartFileByBinaryContent(project, virtualFile)
            else
                formatDartFileByFileEditor(project, fileEditor)
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

    private fun formatDartFileByBinaryContent(project: Project, virtualFile: VirtualFile): FormatResultType
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
        val formatResultText = formatOrReport(project, inputText, virtualFile) ?: return FormatResultType.Error
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

    private fun formatDartFileByFileEditor(project: Project, fileEditor: FileEditor): FormatResultType
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
        val formatResultText = formatOrReport(project, inputText, fileEditor.file) ?: return FormatResultType.Error
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

    private fun formatOrReport(project: Project, inputText: String, virtualFile: VirtualFile): String?
    {
        val methodName = "$CLASS_NAME.formatOrReport"

        val formatResult = format(inputText, virtualFile)

        if (formatResult.resultType == ResultType.Error)
        {
            if (formatResult.throwable == null)
            {
                val reportErrorLink = NotificationTools.createReportErrorLink(
                    content = null,
                    gitHubRepo = Constants.REPO_NAME_DART_FORMAT,
                    origin = "$methodName/1", // TODO: remove
                    stackTrace = null,
                    title = formatResult.text
                )
                NotificationTools.notifyError(NotificationInfo(
                    content = null,
                    listOf(reportErrorLink),
                    origin = "$methodName/1", // TODO: remove
                    project = project,
                    title = formatResult.text,
                    virtualFile = virtualFile
                ))
            }
            else
                NotificationTools.reportThrowable(
                    origin = "$methodName/2", // TODO: remove
                    project = project,
                    throwable = formatResult.throwable,
                    virtualFile = virtualFile
                )

            return null
        }

        if (formatResult.resultType == ResultType.Warning)
        {
            NotificationTools.notifyWarning(NotificationInfo(
                content = null,
                links = null,
                origin = "$methodName/3", // TODO: remove
                project = project,
                title = formatResult.text,
                virtualFile = virtualFile
            ))

            return null
        }

        return formatResult.text
    }

    private fun format(inputText: String, virtualFile: VirtualFile): FormatResult
    {
        if (inputText.isEmpty())
            return FormatResult.ok("")

        val config = getConfig()
        val jsonConfig = config.toJson()
        return ExternalDartFormat.instance.formatViaChannel(inputText, jsonConfig, virtualFile)
    }

    private fun getConfig(): DartFormatConfig
    {
        if (DartFormatPersistentStateComponent.instance == null)
            return DartFormatConfig()

        return DartFormatPersistentStateComponent.instance!!.state
    }
}
