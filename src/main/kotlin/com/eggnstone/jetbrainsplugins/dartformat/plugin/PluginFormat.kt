package com.eggnstone.jetbrainsplugins.dartformat.plugin

import com.eggnstone.jetbrainsplugins.dartformat.formatter.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.Tokenizer
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
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
            println("FormatIterator.processFile: $virtualFile")
            return format(virtualFile, project)
        }
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        println("PluginFormat.actionPerformed: $e")

        try
        {
            val project = e.getRequiredData(CommonDataKeys.PROJECT)
            val formatIterator = FormatIterator(this::formatDartFile, project)

            val virtualFiles = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            println("${virtualFiles.size} virtual files:")
            for (virtualFile in virtualFiles)
            {
                // TODO: filter out already visited files!
                println("\n  $virtualFile")
                VfsUtilCore.iterateChildrenRecursively(virtualFile, this::filterDartFiles, formatIterator)
            }
        }
        catch (err: AssertionError)
        {
            println("While formatting: $e:")
            println("$err")
        }
    }

    private fun filterDartFiles(virtualFile: VirtualFile): Boolean
    {
        println("filterDartFiles: $virtualFile")
        println("filterDartFiles: ${virtualFile.isDirectory}")
        println("filterDartFiles: ${virtualFile.extension}")

        return virtualFile.isDirectory || virtualFile.extension == "dart"
    }

    private fun formatDartFile(virtualFile: VirtualFile, project: Project): Boolean
    {
        println("formatDartFile: $virtualFile")

        if (virtualFile.extension != "dart")
        {
            println("  Not a dart file!")
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
        println("formatDartFileByBinaryContent: $virtualFile")

        try
        {
            if (!virtualFile.isWritable)
            {
                println("  !virtualFile.isWritable")
                return false
            }

            println("  Really formatting: $virtualFile")
            ApplicationManager.getApplication().runWriteAction {
                val inputBytes = virtualFile.inputStream.readAllBytes()
                val inputText = String(inputBytes)
                val tokens = Tokenizer().tokenize(inputText)
                val outputText = Formatter().format(tokens)
                val outputBytes = outputText.toByteArray()
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
        println("formatDartFileByFileEditor: $fileEditor")

        if (fileEditor !is TextEditor)
        {
            println("  fileEditor !is TextEditor")
            return false
        }

        val editor = fileEditor.editor

        try
        {
            println("  Really formatting: $editor")
            ApplicationManager.getApplication().runWriteAction {
                val tokens = Tokenizer().tokenize(editor.document.text)
                val formattedText = Formatter().format(tokens)
                editor.document.setText(formattedText)
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
}
