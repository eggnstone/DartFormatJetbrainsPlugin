package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionUiKind
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
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

class FormatAction
{
    companion object
    {
        const val CLASS_NAME = "FormatAction"

        fun getDataWithVirtualFiles(e: AnActionEvent, key: String, virtualFiles: Array<VirtualFile>): Any?
        {
            //Logger.logVerbose("$CLASS_NAME.getDataWithVirtualFiles: $key")

            return when (key)
            {
                "contextComponent" -> e.getData(PlatformCoreDataKeys.CONTEXT_COMPONENT)
                "editor" -> e.getData(CommonDataKeys.EDITOR)
                "host.editor" -> e.getData(CommonDataKeys.HOST_EDITOR)
                "project" -> e.getData(CommonDataKeys.PROJECT)
                "TOOL_WINDOW" -> e.getData(PlatformDataKeys.TOOL_WINDOW)
                "virtualFileArray" -> virtualFiles

                "external.system.view" ->
                {
                    Logger.logError("$CLASS_NAME.getDataWithVirtualFiles: $key")
                    null
                }

                else ->
                {
                    Logger.logError("$CLASS_NAME.getDataWithVirtualFiles: $key")
                    null
                }
            }
        }
    }

    init
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("${CLASS_NAME}.init()")
    }

    fun actionPerformed(e: AnActionEvent, useBuiltInFormatter: Boolean)
    {
        val methodName = "$CLASS_NAME.actionPerformed"

        val project = e.getData(CommonDataKeys.PROJECT) ?: return
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

        // Pre-extract everything we need from AnActionEvent before leaving the EDT.
        // AnActionEvent's DataContext is not safe to query from a background thread once the
        // action handler has returned.
        val selectedVirtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        val presentation = e.presentation
        val inputEvent = e.inputEvent
        val uiKind = e.uiKind

        ProgressManager.getInstance().run(object : Task.Modal(project, "DartFormat", true)
        {
            override fun run(indicator: ProgressIndicator)
            {
                runFormatting(project, useBuiltInFormatter, selectedVirtualFiles, presentation, inputEvent, uiKind, indicator, e)
            }
        })
    }

    private fun runFormatting(
        project: Project,
        useBuiltInFormatter: Boolean,
        selectedVirtualFiles: Array<VirtualFile>?,
        presentation: Presentation,
        inputEvent: java.awt.event.InputEvent?,
        uiKind: ActionUiKind,
        indicator: ProgressIndicator,
        originalEvent: AnActionEvent
    )
    {
        val methodName = "$CLASS_NAME.runFormatting"
        var lastVirtualDartFile: VirtualFile? = null

        try
        {
            val startTime = Date()

            val finalVirtualDartFiles = mutableSetOf<VirtualFile>()
            val finalVirtualNonDartFiles = mutableSetOf<VirtualFile>()

            ApplicationManager.getApplication().runReadAction {
                val collectVirtualFilesIterator = CollectVirtualFilesIterator(finalVirtualDartFiles, finalVirtualNonDartFiles)
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
                        val filter = if (useBuiltInFormatter) null else this::filterDartFiles
                        VfsUtilCore.iterateChildrenRecursively(selectedVirtualFile, filter, collectVirtualFilesIterator)
                    }
                }
            }

            indicator.isIndeterminate = false

            // FileEditorManager.getSelectedEditor must run on the EDT, so snapshot all editor refs
            // up-front and pass them into the BG loop. Stale by the time we format? Acceptable
            // tradeoff vs. one invokeAndWait per file.
            val editorByFile = mutableMapOf<VirtualFile, FileEditor?>()
            ApplicationManager.getApplication().invokeAndWait {
                val fileEditorManager = FileEditorManager.getInstance(project)
                for (virtualFile in finalVirtualDartFiles)
                    editorByFile[virtualFile] = fileEditorManager.getSelectedEditor(virtualFile)
            }

            if (Constants.DEBUG_FAKE_FORMAT_DELAY && finalVirtualDartFiles.isNotEmpty())
                fakeDelayBeforeFirstFormat(indicator)

            // BG phase: for each file, read + format on this thread; collect deferred write closures.
            // The actual write happens later inside a single EDT command (for undo grouping).
            var changedCount = 0
            var warningCount = 0
            var errorCount = 0
            val pendingWrites = mutableListOf<() -> Unit>()

            if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("  ${finalVirtualDartFiles.size} final dart files:")
            for ((index, finalVirtualDartFile) in finalVirtualDartFiles.withIndex())
            {
                indicator.checkCanceled()
                indicator.fraction = index.toDouble() / finalVirtualDartFiles.size.coerceAtLeast(1)
                indicator.text = "Formatting ${finalVirtualDartFile.name}"
                if (finalVirtualDartFiles.size > 1)
                    indicator.text2 = "${index + 1} of ${finalVirtualDartFiles.size}"

                if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("    Final dart file: $finalVirtualDartFile")
                lastVirtualDartFile = finalVirtualDartFile
                val startTime2 = Date()
                val (result, pendingWrite) = formatDartFile(finalVirtualDartFile, editorByFile[finalVirtualDartFile], project, warningCount == 0)
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
                            title = "Took ${seconds2}s to format $finalVirtualDartFile.",
                            virtualFile = null
                        )
                    )

                if (pendingWrite != null)
                    pendingWrites.add(pendingWrite)

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

            // EDT phase: one undo-transparent command groups every write (Dart files + the built-in
            // reformatter for any non-Dart files) into a single Undo step, matching the original
            // behavior before the BG split.
            val hasDartWrites = pendingWrites.isNotEmpty()
            val hasNonDart = useBuiltInFormatter && finalVirtualNonDartFiles.isNotEmpty()
            if (hasDartWrites || hasNonDart)
            {
                indicator.text = if (hasNonDart) "Applying changes and reformatting non-Dart files" else "Applying changes"
                indicator.text2 = ""

                ApplicationManager.getApplication().invokeAndWait {
                    CommandProcessor.getInstance().runUndoTransparentAction {
                        if (hasDartWrites)
                            ApplicationManager.getApplication().runWriteAction {
                                for (write in pendingWrites)
                                    write()
                            }

                        if (hasNonDart)
                        {
                            if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("  ${finalVirtualNonDartFiles.size} final non-dart files.")
                            val dataContext2 = DataContext { dataId -> getDataWithVirtualFiles(originalEvent, dataId, finalVirtualNonDartFiles.toTypedArray()) }
                            val reformatAction = ActionManager.getInstance().getAction(IdeActions.ACTION_EDITOR_REFORMAT)

                            try
                            {
                                val e2: AnActionEvent = AnActionEvent.createEvent(dataContext2, presentation, ActionPlaces.UNKNOWN, uiKind, inputEvent)
                                ActionUtil.invokeAction(reformatAction, e2, null)
                            }
                            catch (ex: Exception)
                            {
                                Logger.logError("Exception in ${CLASS_NAME}.runFormatting/invokeAction: $ex")
                            }
                            catch (t: Throwable)
                            {
                                Logger.logError("Throwable in ${CLASS_NAME}.runFormatting/invokeAction: $t")
                            }
                        }
                    }
                }
            }

            val showNotification = !useBuiltInFormatter || finalVirtualDartFiles.isNotEmpty()
            if (showNotification && (errorCount == 0 || Constants.SHOW_TIMINGS_EVEN_AFTER_ERROR))
            {
                val endTime = Date()
                val diffTime = endTime.time - startTime.time
                val diffTimeText = if (diffTime < 1000) "$diffTime ms" else "${diffTime / 1000.0} s"

                var finalVirtualDartFilesText = "${finalVirtualDartFiles.size} file"
                if (finalVirtualDartFiles.size != 1)
                    finalVirtualDartFilesText += "s"

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

                var title = "Formatting $finalVirtualDartFilesText took $diffTimeText."

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
        catch (e: ProcessCanceledException)
        {
            // User clicked "Cancel" in the modal progress dialog. Let IntelliJ unwind cleanly.
            throw e
        }
        catch (e: Exception)
        {
            NotificationTools.reportThrowable(
                origin = "$methodName/4",
                project = project,
                throwable = e,
                virtualFile = lastVirtualDartFile
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
                virtualFile = lastVirtualDartFile
            )
        }
    }

    private fun fakeDelayBeforeFirstFormat(indicator: ProgressIndicator)
    {
        Logger.logWarning("$CLASS_NAME.fakeDelayBeforeFirstFormat: DEBUG_FAKE_FORMAT_DELAY active; sleeping ~5s. Cancel the modal to test cancellation.")
        val totalMs = 5000L
        val stepMs = 100L
        val startTime = System.currentTimeMillis()
        indicator.text = "Fake delay before formatting (5s) ..."
        indicator.text2 = "Click Cancel to test cancellation"
        while (true)
        {
            val elapsed = System.currentTimeMillis() - startTime
            if (elapsed >= totalMs) break
            indicator.checkCanceled()
            indicator.fraction = elapsed.toDouble() / totalMs
            Thread.sleep(stepMs)
        }
    }

    private fun filterDartFiles(virtualFile: VirtualFile): Boolean = virtualFile.isDirectory || PluginTools.isDartFile(virtualFile)

    private fun format(inputText: String, virtualFile: VirtualFile, project: Project): FormatResult
    {
        if (inputText.isEmpty())
            return FormatResult.ok("")

        val config = DartFormatConfigGetter.get()
        val jsonConfig = config.toJson()
        return ExternalDartFormat.getInstance().formatViaChannel(inputText, jsonConfig, virtualFile, project)
    }

    // Returns the result type plus an optional deferred write closure. The closure must be invoked
    // by the caller inside an EDT runWriteAction so all writes from one batch can be grouped under
    // a single undo command.
    private fun formatDartFile(virtualFile: VirtualFile, fileEditor: FileEditor?, project: Project, notifyWarnings: Boolean): Pair<FormatResultType, (() -> Unit)?>
    {
        try
        {
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

    private fun formatDartFileByBinaryContent(project: Project, virtualFile: VirtualFile, notifyWarnings: Boolean): Pair<FormatResultType, (() -> Unit)?>
    {
        val isWritable = ApplicationManager.getApplication().runReadAction<Boolean> { virtualFile.isWritable }
        if (!isWritable)
        {
            if (Constants.DEBUG_FORMAT_ACTION)
            {
                Logger.logDebug("formatDartFileByBinaryContent: $virtualFile")
                Logger.logDebug("  !virtualFile.isWritable")
            }

            return Pair(FormatResultType.NothingChanged, null)
        }

        val inputText = ApplicationManager.getApplication().runReadAction<String> {
            String(virtualFile.inputStream.readAllBytes())
        }
        val formatOrReportResult = formatOrReport(project, inputText, virtualFile, notifyWarnings)
        val formatResultText = formatOrReportResult.text
        @Suppress("FoldInitializerAndIfToElvis", "RedundantSuppression", "DuplicatedCode")
        if (formatResultText == null)
            return Pair(if (formatOrReportResult.hasWarning) FormatResultType.Warning else FormatResultType.Error, null)

        if (formatResultText == inputText)
        {
            if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("Nothing changed.")
            return Pair(FormatResultType.NothingChanged, null)
        }

        val outputBytes = formatResultText.toByteArray()
        val pendingWrite: () -> Unit = { virtualFile.setBinaryContent(outputBytes) }
        if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("Something changed.")
        return Pair(FormatResultType.SomethingChanged, pendingWrite)
    }

    private fun formatDartFileByFileEditor(project: Project, fileEditor: FileEditor, notifyWarnings: Boolean): Pair<FormatResultType, (() -> Unit)?>
    {
        if (fileEditor !is TextEditor)
        {
            if (Constants.DEBUG_FORMAT_ACTION)
            {
                Logger.logDebug("formatDartFileByFileEditor: $fileEditor")
                Logger.logDebug("  fileEditor !is TextEditor")
            }
            return Pair(FormatResultType.NothingChanged, null)
        }

        val editor = fileEditor.editor
        val document = editor.document
        val inputText = ApplicationManager.getApplication().runReadAction<String> { document.text }
        val formatOrReportResult = formatOrReport(project, inputText, fileEditor.file, notifyWarnings)
        val formatResultText = formatOrReportResult.text
        @Suppress("FoldInitializerAndIfToElvis", "RedundantSuppression")
        if (formatResultText == null)
            return Pair(if (formatOrReportResult.hasWarning) FormatResultType.Warning else FormatResultType.Error, null)

        if (formatResultText == inputText)
        {
            if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("Nothing changed.")
            return Pair(FormatResultType.NothingChanged, null)
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

        val pendingWrite: () -> Unit = { document.setText(fixedOutputText) }
        if (Constants.DEBUG_FORMAT_ACTION) Logger.logDebug("Something changed.")
        return Pair(FormatResultType.SomethingChanged, pendingWrite)
    }

    private fun formatOrReport(project: Project, inputText: String, virtualFile: VirtualFile, notifyWarnings: Boolean): FormatOrReportResult
    {
        val methodName = "$CLASS_NAME.formatOrReport"

        if (ExternalDartFormat.getInstance().state != ExternalDartFormatState.STARTED)
        {
            ExternalDartFormat.getInstance().notifyWhenReady = true
            var message = when (ExternalDartFormat.getInstance().state)
            {
                ExternalDartFormatState.FAILED_TO_INSTALL -> "failed to install"
                ExternalDartFormatState.FAILED_TO_START -> "failed to start"
                ExternalDartFormatState.FAILED_TO_UPDATE -> "failed to update"
                ExternalDartFormatState.INSTALLING -> "is still installing"
                ExternalDartFormatState.NOT_STARTED -> "hasn't started yet"
                ExternalDartFormatState.STARTED -> throw IllegalStateException("External dart_format should not be started.")
                ExternalDartFormatState.STARTING -> "is still starting"
                ExternalDartFormatState.STOPPED -> "has stopped"
                ExternalDartFormatState.STOPPING -> "is stopping"
                ExternalDartFormatState.UPDATING -> "is still updating"
            }

            message = if (ExternalDartFormat.getInstance().state == ExternalDartFormatState.INSTALLING
                || ExternalDartFormat.getInstance().state == ExternalDartFormatState.STARTING
                || ExternalDartFormat.getInstance().state == ExternalDartFormatState.UPDATING
            )
                "Cannot format yet because external dart_format $message."
            else
                "Cannot format because external dart_format $message."

            val hasFatalError = (ExternalDartFormat.getInstance().state == ExternalDartFormatState.FAILED_TO_INSTALL
                || ExternalDartFormat.getInstance().state == ExternalDartFormatState.FAILED_TO_START)

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

        return when (formatResult.resultType)
        {
            ResultType.Error ->
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
                            links = listOf(reportErrorLink),
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

                FormatOrReportResult(null, hasWarning = false, hasFatalError = false)
            }

            ResultType.Warning ->
            {
                if (notifyWarnings)
                {
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
                }

                FormatOrReportResult(null, hasWarning = true, hasFatalError = false)
            }

            ResultType.Ok ->
            {
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
                            links = listOf(reportErrorLink),
                            origin = "$methodName/8",
                            project = project,
                            title = title,
                            virtualFile = virtualFile
                        )
                    )

                    FormatOrReportResult(null, hasWarning = false, hasFatalError = false)
                }
                else
                    FormatOrReportResult(formatResult.text, hasWarning = false, hasFatalError = false)
            }
        }
    }
}
