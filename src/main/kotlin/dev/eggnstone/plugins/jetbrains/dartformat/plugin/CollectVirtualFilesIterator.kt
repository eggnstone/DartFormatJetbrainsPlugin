package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.roots.ContentIterator
import com.intellij.openapi.vfs.VirtualFile
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.tools.PluginTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class CollectVirtualFilesIterator(private val virtualDartFiles: MutableSet<VirtualFile>, private val virtualNonDartFiles: MutableSet<VirtualFile>) : ContentIterator
{
    override fun processFile(virtualFile: VirtualFile): Boolean
    {
        if (PluginTools.isDartFile(virtualFile))
        {
            if (Constants.DEBUG_COLLECT_VIRTUAL_FILES_ITERATOR) Logger.logDebug("CollectVirtualFilesIterator.processFile: Dart:     $virtualFile")
            virtualDartFiles.add(virtualFile)
        }
        else if (PluginTools.isNonDartFile(virtualFile))
        {
            if (Constants.DEBUG_COLLECT_VIRTUAL_FILES_ITERATOR) Logger.logDebug("CollectVirtualFilesIterator.processFile: Non-Dart: $virtualFile")
            virtualNonDartFiles.add(virtualFile)
        }
        else
        {
            if (Constants.DEBUG_COLLECT_VIRTUAL_FILES_ITERATOR) Logger.logDebug("CollectVirtualFilesIterator.processFile: Other:    $virtualFile")
        }

        return true
    }
}
