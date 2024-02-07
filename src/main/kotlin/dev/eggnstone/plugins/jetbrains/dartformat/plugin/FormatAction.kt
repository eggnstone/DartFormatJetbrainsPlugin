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
import dev.eggnstone.plugins.jetbrains.dartformat.ResultType
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatPersistentStateComponent
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.PluginTools
import java.util.*

class FormatAction : AnAction()
{
    companion object
    {
        const val CLASS_NAME = "FormatAction"
        private const val DEBUG_FORMAT_ACTION = false
    }

    init
    {
        Logger.logDebug("FormatAction: init")
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        val methodName = "$CLASS_NAME.actionPerformed"

        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        var lastFileName: String? = null

        val config = getConfig()

        if (config.hasNothingEnabled())
        {
            val title = "No formatting option enabled"
            val content = //"<html><body>" +
                "Please enable your desired formatting options:" +
                "<pre>File -&gt; Settings -&gt; Other Settings -&gt; DartFormat</pre>" //+
                //"</body></html>"
            NotificationTools.notifyWarning(NotificationInfo(
                content = content,
                fileName = null,
                links = null,
                origin = "$methodName/1",
                project = project,
                title = title
            ))
            return
        }

        if (!config.acceptBeta)
        {
            val title = "Beta version not accepted"
            val content = "<html><body>" +
                "Please accept that this is a beta version and not everything works as it should:" +
                "<pre>File -&gt; Settings -&gt; Other Settings -&gt; DartFormat</pre>" +
                "</body></html>"
            NotificationTools.notifyWarning(NotificationInfo(
                content = content,
                fileName = null,
                links = null,
                origin = "$methodName/2",
                project = project,
                title = title
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
                if (DEBUG_FORMAT_ACTION) Logger.logDebug("No files selected.")
            }
            else
            {
                if (DEBUG_FORMAT_ACTION) Logger.logDebug("${selectedVirtualFiles.size} selected files:")
                for (selectedVirtualFile in selectedVirtualFiles)
                {
                    if (DEBUG_FORMAT_ACTION) Logger.logDebug("  Selected file: $selectedVirtualFile")
                    VfsUtilCore.iterateChildrenRecursively(selectedVirtualFile, this::filterDartFiles, collectVirtualFilesIterator)
                }
            }

            var changedFiles = 0
            var encounteredError = false
            if (DEBUG_FORMAT_ACTION) Logger.logDebug("${finalVirtualFiles.size} final files:")
            CommandProcessor.getInstance().runUndoTransparentAction {
                for (finalVirtualFile in finalVirtualFiles)
                {
                    if (DEBUG_FORMAT_ACTION) Logger.logDebug("  Final file: $finalVirtualFile")
                    lastFileName = finalVirtualFile.path
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

                val title = "Formatting $finalVirtualFilesText took $diffTimeText.\n$changedFilesText changed."
                NotificationTools.notifyInfo(NotificationInfo(
                    content = null,
                    fileName = null,
                    links = null,
                    origin = null,
                    project = project,
                    title = title
                ))
            }
        }
        catch (e: Exception)
        {
            NotificationTools.reportThrowable(
                fileName = lastFileName,
                origin = "$methodName/3",
                project = project,
                throwable = e
            )
        }
        catch (e: Error)
        {
            // catch errors, too, in order to report all problems, e.g.:
            // - java.lang.AssertionError: Wrong line separators: '...\r\n...'
            NotificationTools.reportThrowable(
                fileName = lastFileName,
                origin = "$methodName/4",
                project = project,
                throwable = e
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
            if (DEBUG_FORMAT_ACTION)
            {
                Logger.logDebug("formatDartFileByBinaryContent: $virtualFile")
                Logger.logDebug("  !virtualFile.isWritable")
            }

            return FormatResultType.NothingChanged
        }

        val inputBytes = virtualFile.inputStream.readAllBytes()
        val inputText = String(inputBytes)
        val formatResultText = formatOrReport(project, inputText, virtualFile.path) ?: return FormatResultType.Error
        if (formatResultText == inputText)
        {
            if (DEBUG_FORMAT_ACTION) Logger.logDebug("Nothing changed.")
            return FormatResultType.NothingChanged
        }

        val outputBytes = formatResultText.toByteArray()
        ApplicationManager.getApplication().runWriteAction {
            virtualFile.setBinaryContent(outputBytes)
        }

        if (DEBUG_FORMAT_ACTION) Logger.logDebug("Something changed.")
        return FormatResultType.SomethingChanged
    }

    private fun formatDartFileByFileEditor(project: Project, fileEditor: FileEditor): FormatResultType
    {
        if (fileEditor !is TextEditor)
        {
            if (DEBUG_FORMAT_ACTION)
            {
                Logger.logDebug("formatDartFileByFileEditor: $fileEditor")
                Logger.logDebug("  fileEditor !is TextEditor")
            }
            return FormatResultType.NothingChanged
        }

        val editor = fileEditor.editor

        val document = editor.document
        val inputText = document.text
        val formatResultText = formatOrReport(project, inputText, fileEditor.file.path) ?: return FormatResultType.Error
        if (formatResultText == inputText)
        {
            if (DEBUG_FORMAT_ACTION) Logger.logDebug("Nothing changed.")
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

        if (DEBUG_FORMAT_ACTION) Logger.logDebug("Something changed.")
        return FormatResultType.SomethingChanged
    }

    private fun formatOrReport(project: Project, inputText: String, fileName: String): String?
    {
        val methodName = "$CLASS_NAME.formatOrReport"

        val formatResult = format(inputText, fileName)

        if (formatResult.resultType == ResultType.Error)
        {
            if (formatResult.throwable == null)
                NotificationTools.notifyError(NotificationInfo(
                    content = null,
                    fileName = fileName,
                    links = null,
                    origin = "$methodName/1", // TODO: remove
                    project = project,
                    title = formatResult.text
                ))
            else
                NotificationTools.reportThrowable(
                    fileName = fileName,
                    origin = "$methodName/2", // TODO: remove
                    project = project,
                    throwable = formatResult.throwable
                )

            return null
        }

        if (formatResult.resultType == ResultType.Warning)
        {
            NotificationTools.notifyWarning(NotificationInfo(
                content = null,
                fileName = fileName,
                links = null,
                origin = "$methodName/3", // TODO: remove
                project = project,
                title = formatResult.text
            ))

            return null
        }

        //Logger.log("formatOrReport: ${formatResult.text}")
        return formatResult.text
    }

    private fun format(inputText: String, fileName: String): FormatResult
    {
        if (inputText.isEmpty())
            return FormatResult.ok("")

        val config = getConfig()
        if (!config.acceptBeta)
            return FormatResult.error("Beta version not accepted.")

        val jsonConfig = config.toJson()
        //Logger.logDebug("FormatAction.format: jsonConfig: $jsonConfig")

        return ExternalDartFormat.instance.formatViaChannel(inputText, jsonConfig, fileName)
    }

    private fun getConfig(): DartFormatConfig
    {
        if (DartFormatPersistentStateComponent.instance == null)
            return DartFormatConfig()

        return DartFormatPersistentStateComponent.instance!!.state
    }
}
