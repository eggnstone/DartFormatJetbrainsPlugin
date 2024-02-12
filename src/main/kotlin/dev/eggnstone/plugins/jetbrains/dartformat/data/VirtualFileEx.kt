package dev.eggnstone.plugins.jetbrains.dartformat.data

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile

class VirtualFileEx(fileEditorManager: FileEditorManager, val virtualFile: VirtualFile)
{
    val fileEditor: FileEditor? = fileEditorManager.getSelectedEditor(virtualFile)
}
