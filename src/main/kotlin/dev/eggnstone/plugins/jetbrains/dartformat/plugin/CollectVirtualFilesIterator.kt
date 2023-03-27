package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.roots.ContentIterator
import com.intellij.openapi.vfs.VirtualFile
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger

class CollectVirtualFilesIterator(private val virtualFiles: MutableSet<VirtualFile>) : ContentIterator
{
    override fun processFile(virtualFile: VirtualFile): Boolean
    {
        if (PluginTools.isDartFile(virtualFile))
        {
            DotlinLogger.log("    CollectVirtualFilesIterator.processFile: OK: $virtualFile")
            virtualFiles.add(virtualFile)
        }
        else
        {
            DotlinLogger.log("    CollectVirtualFilesIterator.processFile: Not a dart file: $virtualFile")
        }

        return true
    }
}
