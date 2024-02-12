package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.ResultType
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig
import dev.eggnstone.plugins.jetbrains.dartformat.data.Format2Info
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
import kotlin.concurrent.thread

class Formatter(private val project: Project, private val config: DartFormatConfig)
{
    companion object
    {
        const val CLASS_NAME = "Formatter"
    }

    suspend fun format(selectedVirtualFiles: Array<VirtualFile>?)
    {
        val methodName = "$CLASS_NAME.format"
        logDebug("$methodName(selectedVirtualFiles: ${selectedVirtualFiles?.size} items)")

        var lastFileName: String? = null

        try
        {
            val startTime = Date()

            val virtualFiles = mutableSetOf<VirtualFile>()
            val collectVirtualFilesIterator = CollectVirtualFilesIterator(virtualFiles)

            if (!selectedVirtualFiles.isNullOrEmpty())
            {
                logDebug("1")
                val selectedVirtualFile = selectedVirtualFiles[0]
                logDebug("Selected file: $selectedVirtualFile")
                val virtualFileEx = VirtualFileEx(FileEditorManager.getInstance(project), selectedVirtualFile)
                logDebug("  virtualFileEx.virtualFile: ${virtualFileEx.virtualFile}")
                logDebug("  virtualFileEx.fileEditor:  ${virtualFileEx.fileEditor}")

                val progressManager = ProgressManager.getInstance()

                logInfo("runUndoTransparentAction / runProcessWithProgressSynchronously START")
                logDebug("  isDispatchThread: " + EventQueue.isDispatchThread())
                CommandProcessor.getInstance().runUndoTransparentAction {
                    logDebug("  isDispatchThread: " + EventQueue.isDispatchThread())
                    progressManager.runProcessWithProgressSynchronously(
                        {
                            logDebug("  isDispatchThread: " + EventQueue.isDispatchThread())
                            logDebug("  Calling formatDartFile")
                            val result: FormatResult = runBlocking { formatDartFile(virtualFileEx) }
                            logDebug("  Called  formatDartFile")
                            logDebug("  Calling writeDartFile")
                            runBlocking {
                                //withContext(Dispatchers.Main) {
                                writeTextToVirtualFileEx(virtualFileEx, result)
                            }
                            logDebug("  Called  writeDartFile")
                        },
                        "runUndoTransparentAction / runProcessWithProgressSynchronously",
                        true, // canBeCancelled
                        project
                    )
                }
                logInfo("runUndoTransparentAction / runProcessWithProgressSynchronously END")

                /*logInfo("runUndoTransparentAction / runProcessWithProgressSynchronously START")
                CommandProcessor.getInstance().runUndoTransparentAction {
                    progressManager.runProcessWithProgressSynchronously(
                        {
                            logDebug("  Calling formatDartFile")
                            val result: FormatResult = runBlocking { formatDartFile(virtualFileEx) }
                            logDebug("  Called  formatDartFile")
                            logDebug("  Calling writeDartFile")
                            writeTextToVirtualFileEx(virtualFileEx, result)
                            logDebug("  Called  writeDartFile")
                        },
                        "runUndoTransparentAction / runProcessWithProgressSynchronously",
                        true, // canBeCancelled
                        project
                    )
                }
                logInfo("runUndoTransparentAction / runProcessWithProgressSynchronously END")*/

                /*logInfo("runProcessWithProgressSynchronously / runUndoTransparentAction START")
                progressManager.runProcessWithProgressSynchronously(
                    {
                        CommandProcessor.getInstance().runUndoTransparentAction {
                            logDebug("  Calling formatDartFile")
                            val result: FormatResult = runBlocking { formatDartFile(virtualFileEx) }
                            logDebug("  Called  formatDartFile")
                            logDebug("  Calling writeDartFile")
                            writeTextToVirtualFileEx(virtualFileEx, result)
                            logDebug("  Called  writeDartFile")
                        }
                    },
                    "runProcessWithProgressSynchronously / runUndoTransparentAction",
                    true, // canBeCancelled
                    project
                )
                logInfo("runProcessWithProgressSynchronously / runUndoTransparentAction END")*/

                return
            }

            logDebug("2 ----------------")
            return

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

            logDebug("${virtualFiles.size} files:")

            // Accessing FileEditorManager.getSelectedEditor() inside ProgressManager.runProcessWithProgressSynchronously()
            // throws an exception about the wrong thread: EventQueue.isDispatchThread()=false
            // Therefore, we have to access it outside of this method.
            val fileEditorManager = FileEditorManager.getInstance(project)
            val virtualFilesEx = virtualFiles.map { VirtualFileEx(fileEditorManager, it) }

            var format2Info: Format2Info? = null
            val progressManager = ProgressManager.getInstance()
            CommandProcessor.getInstance().runUndoTransparentAction {
                progressManager.runProcessWithProgressSynchronously(
                    {
                        runBlocking {
                            format2Info = format2(virtualFilesEx, progressManager.progressIndicator!!)
                        }
                    },
                    "DartFormat",
                    true, // canBeCancelled
                    project
                )
            }

            val format2InfoNotNull = format2Info ?: throw DartFormatException.localError("format2Info == null")
            lastFileName = format2InfoNotNull.lastFileName

            if (!format2InfoNotNull.encounteredError)
            {
                val endTime = Date()
                val diffTime = endTime.time - startTime.time
                val diffTimeText = if (diffTime < 1000) "$diffTime ms" else "${diffTime / 1000.0} s"

                var virtualFilesText = "${virtualFiles.size} file"
                if (virtualFiles.size != 1)
                    virtualFilesText += "s"

                val changedFilesText = StringTools.getNumberedText(format2InfoNotNull.changedFiles, "file", "files")
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

    private suspend fun writeTextToVirtualFileEx(virtualFileEx: VirtualFileEx, result: FormatResult)
    {
        if (virtualFileEx.fileEditor == null)
            writeTextToVirtualFile(virtualFileEx.virtualFile, result)
        else
            writeTextToFileEditor(virtualFileEx.fileEditor, result)
    }

    private suspend fun writeTextToFileEditor(fileEditor: FileEditor, result: FormatResult)
    {
        if (fileEditor !is TextEditor)
            throw DartFormatException.localError("fileEditor !is TextEditor")

        /*logDebug("writeTextToFileEditor 1")
        runWriteAction {
            logDebug("writeTextToFileEditor runWriteAction")
            runBlocking {
                logDebug("writeTextToFileEditor runBlocking")
                withContext(Dispatchers.IO) {
                    logDebug("writeTextToFileEditor withContext")
                    fileEditor.editor.document.setText(result.text)
                    logDebug("writeTextToFileEditor DONE")
                }
            }
        }*/

        logDebug("writeTextToFileEditor 2")
        runBlocking {
            logDebug("writeTextToFileEditor runBlocking")
            withContext(Dispatchers.IO) {
                logDebug("writeTextToFileEditor withContext")
                runWriteAction {
                    logDebug("writeTextToFileEditor runWriteAction")
                    fileEditor.editor.document.setText("/*${Date()}*/\n" + result.text)
                    logDebug("writeTextToFileEditor DONE")
                }
            }
        }

        //runWriteAction { fileEditor.editor.document.setText(result.text) }
    }

    private fun writeTextToVirtualFile(virtualFile: VirtualFile, result: FormatResult)
    {
        logDebug("writeDartFileByBinaryContent: $virtualFile")
        virtualFile.setBinaryContent(result.text.toByteArray())
    }

    private suspend fun format2(
        virtualFilesEx: List<VirtualFileEx>,
        progressIndicator: ProgressIndicator
    ): Format2Info
    {
        val methodName = "$CLASS_NAME.format2"
        logDebug("$methodName($virtualFilesEx)")

        var lastFileName: String? = null
        var changedFiles = 0
        var encounteredError = false

        Logger.logDebug("3 ${EventQueue.isDispatchThread()}")
        progressIndicator.isIndeterminate = false
        progressIndicator.text = "Formatting"
        progressIndicator.fraction = 0.0

        for ((index, virtualFileEx) in virtualFilesEx.withIndex())
        {
            var shortPath = virtualFileEx.virtualFile.path
            if (shortPath.startsWith(project.basePath!!))
                shortPath = "..." + shortPath.substring(project.basePath!!.length)
            progressIndicator.text2 = shortPath.replace('/', File.separatorChar)

            logDebug("  File: ${virtualFileEx.virtualFile}")
            lastFileName = virtualFileEx.virtualFile.path

            val result = formatDartFile(virtualFileEx)

            //if (result == FormatResultType.Error)
            if (result.resultType != ResultType.Ok)
            {
                Logger.logError("Error formatting ${virtualFileEx.virtualFile}")
                encounteredError = true
                break
            }

            // TODO
            /*if (result == FormatResultType.SomethingChanged)
                changedFiles++*/

            if (progressIndicator.isCanceled)
                break

            progressIndicator.fraction = index / virtualFilesEx.size.toDouble()
        }

        return Format2Info(lastFileName, encounteredError, changedFiles)
    }

    // TODO: split
    // 1. read text from file
    // 2. format text
    // 3. write text to file
    private suspend fun formatDartFile(virtualFileEx: VirtualFileEx): FormatResult
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

    private suspend fun formatDartFileByBinaryContent(virtualFile: VirtualFile): FormatResult
    {
        val methodName = "$CLASS_NAME.formatDartFileByBinaryContent"
        logDebug("$methodName($virtualFile)")

        if (!virtualFile.isWritable)
        {
            logDebug("formatDartFileByBinaryContent: $virtualFile")
            logDebug("  !virtualFile.isWritable")

            return FormatResult.error("virtualFile !isWritable")
            //return FormatResultType.NothingChanged
        }

        val inputBytes = withContext(Dispatchers.IO) { virtualFile.inputStream.readAllBytes() }
        val inputText = String(inputBytes)
        return formatText(inputText, virtualFile.path)
        val formatResult = formatText(inputText, virtualFile.path)
        if (formatResult.resultType != ResultType.Ok)
        {
            reportFormatResult(virtualFile.path, formatResult)
            return FormatResult.error("TODO")
            //return FormatResultType.Error
        }

        if (formatResult.text == inputText)
        {
            logDebug("Nothing changed.")
            return FormatResult.error("TODO")
            //return FormatResultType.NothingChanged
        }

        logDebug("1 inputText:\n${StringTools.toDisplayString(inputText, 50)}")
        logDebug("1 formatResultText:\n${StringTools.toDisplayString(formatResult.text, 50)}")

        val outputBytes = formatResult.text.toByteArray()
        ApplicationManager.getApplication().runWriteAction {
            virtualFile.setBinaryContent(outputBytes)
        }
        withContext(Dispatchers.IO) {
            ApplicationManager.getApplication().runWriteAction {
                virtualFile.setBinaryContent(outputBytes)
            }
        }

        logDebug("Something changed.")
        return FormatResult.error("TODO")
        //return FormatResultType.SomethingChanged
    }

    private suspend fun formatDartFileByFileEditor(fileEditor: FileEditor): FormatResult
    {
        val methodName = "$CLASS_NAME.formatDartFileByFileEditor"
        logDebug("$methodName($fileEditor)")

        if (fileEditor !is TextEditor)
            throw DartFormatException.localError("fileEditor !is TextEditor")
        /*if (fileEditor !is TextEditor)
        {
            logDebug("formatDartFileByFileEditor: $fileEditor")
            logDebug("  fileEditor !is TextEditor")
            //return FormatResultType.NothingChanged
            return FormatResult.error("fileEditor !is TextEditor")
        }*/

        val editor = fileEditor.editor

        val document = editor.document
        val inputText = document.text

        return formatText(inputText, fileEditor.file.path)
        val formatResult = formatText(inputText, fileEditor.file.path)
        if (formatResult.resultType != ResultType.Ok)
        {
            reportFormatResult(fileEditor.file.path, formatResult)
            return FormatResult.error("TODO")
            //return FormatResultType.Error
        }

        if (formatResult.text == inputText)
        {
            logDebug("Nothing changed.")
            return FormatResult.error("TODO")
            //return FormatResultType.NothingChanged
        }

        val fixedOutputText: String = if (formatResult.text.contains("\r\n"))
        {
            Logger.logDebug("#################################################")
            Logger.logDebug("Why does the outputText contain wrong linebreaks?")
            Logger.logDebug("#################################################")
            formatResult.text.replace("\r\n", "\n")
        }
        else
            formatResult.text

        logDebug("2.1 inputText:\n${StringTools.toDisplayString(inputText, 50)}")
        logDebug("2.2 formatResultText:\n${StringTools.toDisplayString(formatResult.text, 50)}")

        /*     logDebug("2.3a")
          ApplicationManager.getApplication().runWriteAction {
             logDebug("2.3b")
         }

         ApplicationManager.getApplication().runWriteAction {
             logDebug("2.4")
             document.setText(fixedOutputText)
             logDebug("2.5")
         }*/

        /*withContext(Dispatchers.IO) {
            ApplicationManager.getApplication().runWriteAction {
                document.setText(fixedOutputText)
            }
        }*/

        logDebug("Something changed.")
        return FormatResult.error("TODO")
        //return FormatResultType.SomethingChanged
    }

    private fun reportFormatResult(fileName: String, formatResult: FormatResult)
    {
        if (formatResult.resultType == ResultType.Error)
        {
            if (formatResult.throwable == null)
            {
                val reportErrorLink = NotificationTools.createReportErrorLink(
                    content = null,
                    gitHubRepo = Constants.REPO_NAME_DART_FORMAT,
                    origin = null,
                    stackTrace = null,
                    title = formatResult.text
                )
                NotificationTools.notifyError(NotificationInfo(
                    content = null,
                    fileName = fileName,
                    listOf(reportErrorLink),
                    origin = null,
                    project = project,
                    title = formatResult.text
                ))
            }
            else
                NotificationTools.reportThrowable(
                    fileName = fileName,
                    origin = null,
                    project = project,
                    throwable = formatResult.throwable
                )

            return
        }

        if (formatResult.resultType == ResultType.Warning)
        {
            NotificationTools.notifyWarning(NotificationInfo(
                content = null,
                fileName = fileName,
                links = null,
                origin = null,
                project = project,
                title = formatResult.text
            ))

            return
        }

        return
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

    private fun logInfo(s: String)
    {
        if (Constants.DEBUG_FORMATTER)
            Logger.logInfo(s)
    }
}
