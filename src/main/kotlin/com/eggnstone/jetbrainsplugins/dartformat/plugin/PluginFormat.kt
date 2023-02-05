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
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile

class PluginFormat : AnAction()
{
    override fun actionPerformed(e: AnActionEvent)
    {
        try
        {
            val project = e.getRequiredData(CommonDataKeys.PROJECT)
            val virtualFiles = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            println("${virtualFiles.size} virtual files:")
            for (virtualFile in virtualFiles)
                formatVirtualFile(virtualFile, project)
        }
        catch (err: AssertionError)
        {
            println("While formatting: $e:")
            println("$err")
        }
    }

    private fun formatVirtualFile(virtualFile: VirtualFile, project: Project)
    {
        println("formatVirtualFile: $virtualFile")

        if (virtualFile.isDirectory)
        {
            formatDirectory(virtualFile)
            return
        }

        if (virtualFile.extension == "dart")
        {
            formatDartFile(virtualFile, project)
            return
        }
    }

    private fun formatDirectory(virtualFile: VirtualFile)
    {
        println("formatDirectory: $virtualFile")

        VfsUtilCore.iterateChildrenRecursively(virtualFile, this::filterDartFiles, this::contentIterator)
    }

    private fun contentIterator(virtualFile: VirtualFile): Boolean
    {
        println("contentIterator: $virtualFile")

        return false
    }

    private fun filterDartFiles(virtualFile: VirtualFile): Boolean
    {
        println("filterDartFiles: $virtualFile")
        println("filterDartFiles: ${virtualFile.isDirectory}")
        println("filterDartFiles: ${virtualFile.extension}")

        return virtualFile.isDirectory || virtualFile.extension == "dart"
    }

    private fun formatDartFile(virtualFile: VirtualFile, project: Project)
    {
        println("formatDartFile: $virtualFile")

        try
        {
            val fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(virtualFile)
            if (fileEditor == null)
                formatDartFileByBinaryContent(virtualFile)
            else
                formatDartFileByFileEditor(fileEditor)
        }
        catch (err: AssertionError)
        {
            println("While formatting: $virtualFile:")
            println("$err")
        }
    }

    private fun formatDartFileByBinaryContent(virtualFile: VirtualFile)
    {
        println("formatDartFileByBinaryContent: $virtualFile")

        try
        {
            if (!virtualFile.isWritable)
            {
                println("  !virtualFile.isWritable")
                return
            }

            println("  Really formatting: $virtualFile")
            ApplicationManager.getApplication().runWriteAction {
                val inputBytes = virtualFile.inputStream.readAllBytes()
                val s: String = "//Test\n" + String(inputBytes)
                val outputBytes = s.toByteArray()
                virtualFile.setBinaryContent(outputBytes)
            }
        }
        catch (err: AssertionError)
        {
            println("While formatting: $virtualFile:")
            println("$err")
        }
    }

    private fun formatDartFileByFileEditor(fileEditor: FileEditor)
    {
        println("formatDartFileByFileEditor: $fileEditor")

        if (fileEditor !is TextEditor)
        {
            println("  fileEditor !is TextEditor")
            return
        }

        val editor = fileEditor.editor

        try
        {
            println("  Really formatting: $fileEditor")
            ApplicationManager.getApplication().runWriteAction {
                val tokens = Tokenizer().tokenize(editor.document.text)
                val formattedText = Formatter().format(tokens)
                editor.document.setText(formattedText)
            }
        }
        catch (err: AssertionError)
        {
            println("While formatting: $fileEditor:")
            println("$err")
        }
    }
}
