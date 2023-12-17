package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.roots.ContentIterator
import com.intellij.openapi.vfs.VirtualFile

class CollectVirtualFilesIterator(private val virtualFiles: MutableSet<VirtualFile>) : ContentIterator
{
    override fun processFile(virtualFile: VirtualFile): Boolean
    {
        if (PluginTools.isDartFile(virtualFile))
        {
            //Logger.log("    CollectVirtualFilesIterator.processFile: OK: $virtualFile")
            virtualFiles.add(virtualFile)
        }
        else
        {
            //Logger.log("    CollectVirtualFilesIterator.processFile: Not a dart file: $virtualFile")
        }

        return true
    }
}
