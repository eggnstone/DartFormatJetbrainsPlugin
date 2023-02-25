package com.eggnstone.jetbrainsplugins.dartformat.plugin

import com.eggnstone.jetbrainsplugins.dartformat.config.DartFormatConfig
import com.eggnstone.jetbrainsplugins.dartformat.config.DartFormatPersistentStateComponent
import com.eggnstone.jetbrainsplugins.dartformat.formatters.FormatterWithConfig
import com.eggnstone.jetbrainsplugins.dartformat.indenter.IndenterWithConfig
import com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier.SimpleBlockifier
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
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

typealias FormatHandler = (virtualFile: VirtualFile, project: Project) -> Boolean

class PluginFormat : AnAction()
{
    private class FormatIterator(private val format: FormatHandler, private val project: Project) : ContentIterator
    {
        override fun processFile(virtualFile: VirtualFile): Boolean
        {
            //println("FormatIterator.processFile: $virtualFile")
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
                    //println("\n  $virtualFile")
                    VfsUtilCore.iterateChildrenRecursively(virtualFile, this::filterDartFiles, formatIterator)
                }
            }
        }
        catch (err: AssertionError)
        {
            println("While formatting: $e:")
            println("$err")
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
            //println("formatDartFile: $virtualFile")
            //println("  Not a dart file!")
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
            println("While formatting: $virtualFile:")
            println("$err")
            return false
        }
    }

    private fun formatDartFileByBinaryContent(virtualFile: VirtualFile): Boolean
    {
        try
        {
            if (!virtualFile.isWritable)
            {
                println("formatDartFileByBinaryContent: $virtualFile")
                println("  !virtualFile.isWritable")
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
            println("While formatting: $virtualFile:")
            println("$err")
            return false
        }
    }

    private fun formatDartFileByFileEditor(fileEditor: FileEditor): Boolean
    {
        if (fileEditor !is TextEditor)
        {
            println("formatDartFileByFileEditor: $fileEditor")
            println("  fileEditor !is TextEditor")
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
            println("While formatting: $fileEditor:")
            println("$err")
            return false
        }
    }

    private fun format(inputText: String): String
    {
        val config = getConfig()

        val simpleBlocks = SimpleBlockifier().blockify(inputText)
        SimpleBlockifier().printBlocks(simpleBlocks)

        val inputTokens = Tokenizer().tokenize(inputText)

        val outputTokens = FormatterWithConfig(config).format(inputTokens)

        @Suppress("UnnecessaryVariable")
        val outputText = IndenterWithConfig(config).indent(outputTokens)

        return outputText
    }

    private fun getConfig(): DartFormatConfig
    {
        if (DartFormatPersistentStateComponent.instance == null)
            return DartFormatConfig()

        return DartFormatPersistentStateComponent.instance!!.state
    }
}
