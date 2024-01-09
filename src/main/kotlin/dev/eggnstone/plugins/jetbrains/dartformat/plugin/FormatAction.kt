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
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.FailType
import dev.eggnstone.plugins.jetbrains.dartformat.ResultType
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatPersistentStateComponent
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.PluginTools
import java.util.*

class FormatAction : AnAction()
{
    companion object
    {
        private const val DEBUG_FORMAT_ACTION = false
    }

    init
    {
        Logger.log("FormatAction: init")
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        val project = e.getRequiredData(CommonDataKeys.PROJECT)

        val config = getConfig()
        if (config == DartFormatConfig())
        {
            val subtitle = "No formatting option enabled"
            val messages = listOf("Please check File -&gt; Settings -&gt; Other Settings -&gt; DartFormat")
            NotificationTools.notifyWarning(messages, project, subtitle)
            return
        }

        try
        {
            val startTime = Date()

            val finalVirtualFiles = mutableSetOf<VirtualFile>()
            val collectVirtualFilesIterator = CollectVirtualFilesIterator(finalVirtualFiles)
            val selectedVirtualFiles = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE_ARRAY)

            if (DEBUG_FORMAT_ACTION) Logger.log("${selectedVirtualFiles.size} selected files:")
            for (selectedVirtualFile in selectedVirtualFiles)
            {
                if (DEBUG_FORMAT_ACTION) Logger.log("  Selected file: $selectedVirtualFile")
                VfsUtilCore.iterateChildrenRecursively(selectedVirtualFile, this::filterDartFiles, collectVirtualFilesIterator)
            }

            var changedFiles = 0
            var encounteredError = false
            if (DEBUG_FORMAT_ACTION) Logger.log("${finalVirtualFiles.size} final files:")
            CommandProcessor.getInstance().runUndoTransparentAction {
                for (finalVirtualFile in finalVirtualFiles)
                {
                    if (DEBUG_FORMAT_ACTION) Logger.log("  Final file: $finalVirtualFile")
                    val result = formatDartFile(finalVirtualFile, project)

                    if (result == FormatResultType.Error)
                    {
                        encounteredError = true
                        break
                    }

                    if (result == FormatResultType.SomethingChanged)
                        changedFiles++
                }
            }

            if (!encounteredError)
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

                val lines = mutableListOf<String>()
                lines.add("Formatting $finalVirtualFilesText took $diffTimeText.")
                lines.add("$changedFilesText changed.")
                NotificationTools.notifyInfo(lines, project)
            }
        }
        catch (e: Exception)
        {
            NotificationTools.reportThrowable(e, project)
        }
        catch (e: Error)
        {
            // catch errors, too, in order to report all problems, e.g.:
            // - java.lang.AssertionError: Wrong line separators: '...\r\n...'
            NotificationTools.reportThrowable(e, project)
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
            throw DartFormatException(FailType.ERROR, "${virtualFile.path}\n${e.message}", e)
        }
        catch (e: Error)
        {
            // necessary?
            throw DartFormatException(FailType.ERROR, "${virtualFile.path}\n${e.message}", e)
        }
    }

    private fun formatDartFileByBinaryContent(project: Project, virtualFile: VirtualFile): FormatResultType
    {
        if (!virtualFile.isWritable)
        {
            if (DEBUG_FORMAT_ACTION)
            {
                Logger.log("formatDartFileByBinaryContent: $virtualFile")
                Logger.log("  !virtualFile.isWritable")
            }

            return FormatResultType.NothingChanged
        }

        val inputBytes = virtualFile.inputStream.readAllBytes()
        val inputText = String(inputBytes)
        val formatResultText = formatOrReport(project, inputText) ?: return FormatResultType.Error
        if (formatResultText == inputText)
        {
            if (DEBUG_FORMAT_ACTION) Logger.log("Nothing changed.")
            return FormatResultType.NothingChanged
        }

        val outputBytes = formatResultText.toByteArray()
        ApplicationManager.getApplication().runWriteAction {
            virtualFile.setBinaryContent(outputBytes)
        }

        if (DEBUG_FORMAT_ACTION) Logger.log("Something changed.")
        return FormatResultType.SomethingChanged
    }

    private fun formatDartFileByFileEditor(project: Project, fileEditor: FileEditor): FormatResultType
    {
        if (fileEditor !is TextEditor)
        {
            if (DEBUG_FORMAT_ACTION)
            {
                Logger.log("formatDartFileByFileEditor: $fileEditor")
                Logger.log("  fileEditor !is TextEditor")
            }
            return FormatResultType.NothingChanged
        }

        val editor = fileEditor.editor

        val document = editor.document
        val inputText = document.text
        val formatResultText = formatOrReport(project, inputText) ?: return FormatResultType.Error
        if (formatResultText == inputText)
        {
            if (DEBUG_FORMAT_ACTION) Logger.log("Nothing changed.")
            return FormatResultType.NothingChanged
        }

        val fixedOutputText: String = if (formatResultText.contains("\r\n"))
        {
            Logger.log("#################################################")
            Logger.log("Why does the outputText contain wrong linebreaks?")
            Logger.log("#################################################")
            formatResultText.replace("\r\n", "\n")
        }
        else
            formatResultText

        ApplicationManager.getApplication().runWriteAction {
            document.setText(fixedOutputText)
        }

        if (DEBUG_FORMAT_ACTION) Logger.log("Something changed.")
        return FormatResultType.SomethingChanged
    }

    private fun formatOrReport(project: Project, inputText: String): String?
    {
        val formatResult = format(inputText)

        if (formatResult.resultType == ResultType.Error)
        {
            if (formatResult.throwable == null)
                NotificationTools.notifyError(formatResult.text, project)
            else
                NotificationTools.reportThrowable(formatResult.throwable, project)
            return null
        }

        if (formatResult.resultType == ResultType.Warning)
        {
            NotificationTools.notifyWarning(listOf(formatResult.text), project)
            return null
        }

        return formatResult.text
    }

    private fun format(inputText: String): FormatResult
    {
        if (inputText.isEmpty())
            return FormatResult.ok("")

        return ExternalDartFormat.instance.formatViaChannel(inputText, getConfig().toJson())
    }

    private fun getConfig(): DartFormatConfig
    {
        if (DartFormatPersistentStateComponent.instance == null)
            return DartFormatConfig()

        return DartFormatPersistentStateComponent.instance!!.state
    }
}
