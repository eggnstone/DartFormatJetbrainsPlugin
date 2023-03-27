package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.notification.*
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
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatPersistentStateComponent
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.MasterSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.tools.SafetyTools
import java.util.*

class PluginFormat : AnAction()
{
    companion object
    {
        private val masterSplitter = MasterSplitter()
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        val project = e.getRequiredData(CommonDataKeys.PROJECT)

        val config = getConfig()
        if (!config.isEnabled)
        {
            val subtitle = "No formatting option enabled"
            val messages = listOf("Please check File -&gt; Settings -&gt; Other Settings -&gt; DartFormat")
            notifyWarning(messages, project, subtitle)
            return
        }

        try
        {
            val startTime = Date()

            val finalVirtualFiles = mutableSetOf<VirtualFile>()
            val collectVirtualFilesIterator = CollectVirtualFilesIterator(finalVirtualFiles)
            val selectedVirtualFiles = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE_ARRAY)

            //DotlinLogger.log("${selectedVirtualFiles.size} selected files:")
            for (selectedVirtualFile in selectedVirtualFiles)
            {
                //DotlinLogger.log("  Selected file: $selectedVirtualFile")
                VfsUtilCore.iterateChildrenRecursively(selectedVirtualFile, this::filterDartFiles, collectVirtualFilesIterator)
            }

            var changedFiles = 0
            //DotlinLogger.log("${finalVirtualFiles.size} final files:")
            CommandProcessor.getInstance().runUndoTransparentAction {
                for (finalVirtualFile in finalVirtualFiles)
                {
                    //DotlinLogger.log("  Final file: $finalVirtualFile")
                    if (formatDartFile(finalVirtualFile, project))
                        changedFiles++
                }
            }

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
            notifyInfo(lines, project)
        }
        catch (err: DartFormatException)
        {
            DotlinLogger.log("DartFormatException: ${err.message}")
            val errMessage = err.message ?: "Unknown error."
            val errMessages = errMessage.split("\n")
            notifyError(errMessages, project)
        }
    }

    private fun notifyInfo(lines: List<String>, project: Project, subtitle: String? = null)
    {
        notifyByToolWindowBalloon(lines, NotificationType.INFORMATION, project, subtitle)
    }

    private fun notifyWarning(lines: List<String>, project: Project, subtitle: String? = null)
    {
        notifyByToolWindowBalloon(lines, NotificationType.WARNING, project, subtitle)
    }

    private fun notifyError(lines: List<String>, project: Project, subtitle: String? = null)
    {
        notifyByToolWindowBalloon(lines, NotificationType.ERROR, project, subtitle)
    }

    private fun notifyByToolWindowBalloon(lines: List<String>, type: NotificationType, project: Project, subtitle: String? = null)
    {
        val combinedLines = lines.joinToString("<br/>")

        val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("DartFormat")
        val notification = notificationGroup.createNotification("DartFormat", combinedLines, type)
        notification.subtitle = subtitle
        notification.notify(project)
    }

    private fun filterDartFiles(virtualFile: VirtualFile): Boolean = virtualFile.isDirectory || PluginTools.isDartFile(virtualFile)

    private fun formatDartFile(virtualFile: VirtualFile, project: Project): Boolean
    {
        try
        {
            val fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(virtualFile)
            return if (fileEditor == null)
                formatDartFileByBinaryContent(virtualFile)
            else
                formatDartFileByFileEditor(fileEditor)
        }
        catch (err: Exception)
        {
            throw DartFormatException("${virtualFile.path}\n${err.message}")
        }
        catch (err: Error)
        {
            throw DartFormatException("${virtualFile.path}\n${err.message}")
        }
    }

    private fun formatDartFileByBinaryContent(virtualFile: VirtualFile): Boolean
    {
        if (!virtualFile.isWritable)
        {
            DotlinLogger.log("formatDartFileByBinaryContent: $virtualFile")
            DotlinLogger.log("  !virtualFile.isWritable")
            return false
        }

        val inputBytes = virtualFile.inputStream.readAllBytes()
        val inputText = String(inputBytes)
        val outputText = format(inputText)
        if (outputText == inputText)
        {
            //DotlinLogger.log("Nothing changed.")
            return false
        }

        val outputBytes = outputText.toByteArray()
        ApplicationManager.getApplication().runWriteAction {
            virtualFile.setBinaryContent(outputBytes)
        }

        //DotlinLogger.log("Something changed.")
        return true
    }

    private fun formatDartFileByFileEditor(fileEditor: FileEditor): Boolean
    {
        if (fileEditor !is TextEditor)
        {
            DotlinLogger.log("formatDartFileByFileEditor: $fileEditor")
            DotlinLogger.log("  fileEditor !is TextEditor")
            return false
        }

        val editor = fileEditor.editor

        val document = editor.document
        val inputText = document.text
        val outputText = format(inputText)
        if (outputText == inputText)
        {
            //DotlinLogger.log("Nothing changed.")
            return false
        }

        ApplicationManager.getApplication().runWriteAction {
            document.setText(outputText)
        }

        //DotlinLogger.log("Something changed.")
        return true
    }

    private fun format(inputText: String): String
    {
        DotlinLogger.isEnabled = false

        try
        {
            val config = getConfig()

            val splitResult = masterSplitter.split(inputText)
            //PartTools.printParts(splitResult.parts)

            val masterIndenter = MasterIndenter(config.indentationSpacesPerLevel)

            @Suppress("UnnecessaryVariable")
            val indentResult = masterIndenter.indentParts(splitResult.parts)

            SafetyTools.checkForUnexpectedChanges(inputText, indentResult)

            return indentResult
        }
        finally
        {
            DotlinLogger.isEnabled = true
        }
    }

    private fun getConfig(): DartFormatConfig
    {
        if (DartFormatPersistentStateComponent.instance == null)
            return DartFormatConfig()

        return DartFormatPersistentStateComponent.instance!!.state
    }
}
