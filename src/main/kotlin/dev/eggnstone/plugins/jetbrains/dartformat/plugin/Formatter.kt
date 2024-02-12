package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.ResultType
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.VirtualFileEx
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.PluginTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.StringTools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.awt.EventQueue
import java.io.File
import java.util.*

class Formatter(private val project: Project, private val config: DartFormatConfig)
{
    companion object
    {
        const val CLASS_NAME = "Formatter"
    }

    fun format(selectedVirtualFiles: Array<VirtualFile>?)
    {
        val methodName = "$CLASS_NAME.format"

        var lastFileName: String? = null

        try
        {
            val startTime = Date()

            val virtualFiles = mutableSetOf<VirtualFile>()
            val collectVirtualFilesIterator = CollectVirtualFilesIterator(virtualFiles)

            if (selectedVirtualFiles == null)
            {
                logDebug("No files selected.")
            }
            else
            {
                // TODO: show progress indicator?
                logDebug("${selectedVirtualFiles.size} selected files:")
                for (selectedVirtualFile in selectedVirtualFiles)
                {
                    logDebug("  Selected file: $selectedVirtualFile")
                    VfsUtilCore.iterateChildrenRecursively(selectedVirtualFile, this::filterDartFiles, collectVirtualFilesIterator)
                }
            }

            var changedFiles = 0
            var encounteredError = false
            logDebug("${virtualFiles.size} files:")

            // Accessing FileEditorManager.getSelectedEditor() inside ProgressManager.runProcessWithProgressSynchronously()
            // throws an exception about the wrong thread: EventQueue.isDispatchThread()=false
            // Therefore, we have to access it outside of this method.
            val fileEditorManager = FileEditorManager.getInstance(project)
            val virtualFilesEx = virtualFiles.map { VirtualFileEx(fileEditorManager, it) }

            CommandProcessor.getInstance().runUndoTransparentAction {
                val progressManager = ProgressManager.getInstance()
                progressManager.runProcessWithProgressSynchronously(
                    {
                        Logger.logDebug("3 ${EventQueue.isDispatchThread()}")
                        val indicator = progressManager.progressIndicator
                        indicator.isIndeterminate = false
                        indicator.text = "Formatting"
                        indicator.fraction = 0.0

                        for ((index, virtualFileEx) in virtualFilesEx.withIndex())
                        {
                            var shortPath = virtualFileEx.virtualFile.path
                            if (shortPath.startsWith(project.basePath!!))
                                shortPath = "..." + shortPath.substring(project.basePath!!.length)
                            indicator.text2 = shortPath.replace('/', File.separatorChar)

                            logDebug("  File: ${virtualFileEx.virtualFile}")
                            lastFileName = virtualFileEx.virtualFile.path

                            val result = runBlocking { formatDartFile(virtualFileEx) }

                            if (result == FormatResultType.Error)
                            {
                                Logger.logError("Error formatting ${virtualFileEx.virtualFile}")
                                encounteredError = true
                                break
                            }

                            if (result == FormatResultType.SomethingChanged)
                                changedFiles++

                            if (indicator.isCanceled)
                                break

                            indicator.fraction = index / virtualFiles.size.toDouble()
                        }
                    },
                    "DartFormat",
                    true, // canBeCancelled
                    project
                )
            }

            if (!encounteredError)
            {
                val endTime = Date()
                val diffTime = endTime.time - startTime.time
                val diffTimeText = if (diffTime < 1000) "$diffTime ms" else "${diffTime / 1000.0} s"

                var virtualFilesText = "${virtualFiles.size} file"
                if (virtualFiles.size != 1)
                    virtualFilesText += "s"

                val changedFilesText: String = when (changedFiles)
                {
                    0 -> "Nothing"
                    1 -> "1 file"
                    else -> "$changedFiles files"
                }

                val title = "Formatting $virtualFilesText took $diffTimeText.\n$changedFilesText changed."
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
                origin = "$methodName/1",
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
                origin = "$methodName/2",
                project = project,
                throwable = e
            )
        }
    }

    private suspend fun formatDartFile(virtualFileEx: VirtualFileEx): FormatResultType
    {
        try
        {
            return if (virtualFileEx.fileEditor == null)
                formatDartFileByBinaryContent(virtualFileEx.virtualFile)
            else
                formatDartFileByFileEditor(virtualFileEx.fileEditor)
        }
        catch (e: DartFormatException)
        {
            throw e
        }
        catch (e: Exception)
        {
            throw DartFormatException.localError("${virtualFileEx.virtualFile.path}\n${e.message}", e)
        }
        catch (e: Error)
        {
            // necessary?
            throw DartFormatException.localError("ERROR ${virtualFileEx.virtualFile.path}\n${e.message}", e)
        }
    }

    private suspend fun formatDartFileByBinaryContent(virtualFile: VirtualFile): FormatResultType
    {
        if (!virtualFile.isWritable)
        {
            logDebug("formatDartFileByBinaryContent: $virtualFile")
            logDebug("  !virtualFile.isWritable")

            return FormatResultType.NothingChanged
        }

        val inputBytes = withContext(Dispatchers.IO) { virtualFile.inputStream.readAllBytes() }
        val inputText = String(inputBytes)
        val formatResultText = formatTextOrReport(inputText, virtualFile.path) ?: return FormatResultType.Error
        if (formatResultText == inputText)
        {
            logDebug("Nothing changed.")
            return FormatResultType.NothingChanged
        }

        logDebug("1 inputText:\n${StringTools.toDisplayString(inputText, 50)}")
        logDebug("1 formatResultText:\n${StringTools.toDisplayString(formatResultText, 50)}")

        val outputBytes = formatResultText.toByteArray()
        withContext(Dispatchers.IO) {
            ApplicationManager.getApplication().runWriteAction {
                virtualFile.setBinaryContent(outputBytes)
            }
        }

        logDebug("Something changed.")
        return FormatResultType.SomethingChanged
    }

    private suspend fun formatDartFileByFileEditor(fileEditor: FileEditor): FormatResultType
    {
        if (fileEditor !is TextEditor)
        {
            logDebug("formatDartFileByFileEditor: $fileEditor")
            logDebug("  fileEditor !is TextEditor")
            return FormatResultType.NothingChanged
        }

        val editor = fileEditor.editor

        val document = editor.document
        val inputText = document.text
        val formatResultText = formatTextOrReport(inputText, fileEditor.file.path) ?: return FormatResultType.Error
        if (formatResultText == inputText)
        {
            logDebug("Nothing changed.")
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

        logDebug("2 inputText:\n${StringTools.toDisplayString(inputText, 50)}")
        logDebug("2 formatResultText:\n${StringTools.toDisplayString(formatResultText, 50)}")

        withContext(Dispatchers.IO) {
            ApplicationManager.getApplication().runWriteAction {
                document.setText(fixedOutputText)
            }
        }

        logDebug("Something changed.")
        return FormatResultType.SomethingChanged
    }

    private suspend fun formatTextOrReport(inputText: String, fileName: String): String?
    {
        val methodName = "$CLASS_NAME.formatTextOrReport"

        val formatResult = formatText(inputText, fileName)

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
                    fileName = fileName,
                    listOf(reportErrorLink),
                    origin = "$methodName/2", // TODO: remove
                    project = project,
                    title = formatResult.text
                ))
            }
            else
                NotificationTools.reportThrowable(
                    fileName = fileName,
                    origin = "$methodName/3", // TODO: remove
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
                origin = "$methodName/4", // TODO: remove
                project = project,
                title = formatResult.text
            ))

            return null
        }

        return formatResult.text
    }

    private suspend fun formatText(inputText: String, fileName: String): FormatResult
    {
        if (inputText.isEmpty())
            return FormatResult.ok("")

        val jsonConfig = config.toJson()
        return ExternalDartFormat.instance.formatViaChannel(inputText, jsonConfig, fileName)
    }

    private fun filterDartFiles(virtualFile: VirtualFile): Boolean //
        = virtualFile.isDirectory || PluginTools.isDartFile(virtualFile)

    private fun logDebug(s: String)
    {
        if (Constants.DEBUG_FORMATTER)
            Logger.logDebug(s)
    }
}
