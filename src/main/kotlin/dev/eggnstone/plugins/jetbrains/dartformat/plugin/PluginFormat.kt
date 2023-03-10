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
import com.intellij.openapi.roots.ContentIterator
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatPersistentStateComponent
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.MasterSplitter

typealias FormatHandler = (virtualFile: VirtualFile, project: Project) -> Boolean

class PluginFormat : AnAction()
{
    companion object
    {
        private val masterSplitter = MasterSplitter()
    }

    private class FormatIterator(private val format: FormatHandler, private val project: Project) : ContentIterator
    {
        override fun processFile(virtualFile: VirtualFile): Boolean
        {
            //DotlinLogger.log("FormatIterator.processFile: $virtualFile")
            return format(virtualFile, project)
        }
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        try
        {
            val project = e.getRequiredData(CommonDataKeys.PROJECT)
            val formatIterator = FormatIterator(this::formatDartFile, project)
            val virtualFiles = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE_ARRAY)

            CommandProcessor.getInstance().runUndoTransparentAction {
                for (virtualFile in virtualFiles)
                {
                    // TODO: filter out already visited files!
                    //DotlinLogger.log("\n  $virtualFile")
                    VfsUtilCore.iterateChildrenRecursively(virtualFile, this::filterDartFiles, formatIterator)
                }
            }
        }
        catch (err: AssertionError)
        {
            DotlinLogger.log("While formatting: $e:")
            DotlinLogger.log("$err")
        }
    }

    private fun filterDartFiles(virtualFile: VirtualFile): Boolean = virtualFile.isDirectory || isDartFile(virtualFile)

    private fun isDartFile(virtualFile: VirtualFile): Boolean
    {
        if (virtualFile.extension != "dart")
            return false

        if (virtualFile.name.endsWith(".freezed.dart"))
            return false

        if (virtualFile.name.endsWith(".g.dart"))
            return false

        if (virtualFile.name.endsWith(".gr.dart"))
            return false

        return true
    }

    private fun formatDartFile(virtualFile: VirtualFile, project: Project): Boolean
    {
        if (!isDartFile(virtualFile))
        {
            //DotlinLogger.log("formatDartFile: $virtualFile")
            //DotlinLogger.log("  Not a dart file!")
            return true
        }

        try
        {
            val fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(virtualFile)
            @Suppress("FoldInitializerAndIfToElvis")
            if (fileEditor == null)
                return formatDartFileByBinaryContent(virtualFile)

            return formatDartFileByFileEditor(fileEditor)
        }
        catch (err: AssertionError)
        {
            DotlinLogger.log("While formatting: $virtualFile:")
            DotlinLogger.log("$err")
            return false
        }
    }

    private fun formatDartFileByBinaryContent(virtualFile: VirtualFile): Boolean
    {
        try
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
                return true

            val outputBytes = outputText.toByteArray()
            ApplicationManager.getApplication().runWriteAction {
                virtualFile.setBinaryContent(outputBytes)
            }

            return true
        }
        catch (err: AssertionError)
        {
            DotlinLogger.log("While formatting: $virtualFile:")
            DotlinLogger.log("$err")
            return false
        }
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

        try
        {
            val document = editor.document
            val inputText = document.text
            val outputText = format(inputText)
            if (outputText == inputText)
                return true

            ApplicationManager.getApplication().runWriteAction {
                document.setText(outputText)
            }

            return true
        }
        catch (err: AssertionError)
        {
            DotlinLogger.log("While formatting: $fileEditor:")
            DotlinLogger.log("$err")
            return false
        }
    }

    private fun format(inputText: String): String
    {
        val config = getConfig()

        val splitResult = masterSplitter.split(inputText)
        //PartTools.printParts(splitResult.parts)

        val masterIndenter = MasterIndenter(config.indentationSpacesPerLevel)

        @Suppress("UnnecessaryVariable")
        val indentResult = masterIndenter.indentParts(splitResult.parts)

        return indentResult

        /*val inputTokens = Tokenizer().tokenize(inputText)

        val outputTokens = FormatterWithConfig(config).format(inputTokens)

        @Suppress("UnnecessaryVariable")
        val outputText = IndenterWithConfig(config).indent(outputTokens)

        return outputText*/
    }

    private fun getConfig(): DartFormatConfig
    {
        if (DartFormatPersistentStateComponent.instance == null)
            return DartFormatConfig()

        return DartFormatPersistentStateComponent.instance!!.state
    }
}
