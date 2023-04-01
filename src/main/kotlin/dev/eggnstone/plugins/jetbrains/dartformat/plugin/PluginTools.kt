package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.vfs.VirtualFile

class PluginTools
{
    companion object
    {
        fun isDartFile(virtualFile: VirtualFile): Boolean
        {
            if (virtualFile.extension != "dart")
                return false

            if (virtualFile.name.endsWith(".freezed.dart"))
                return false

            if (virtualFile.name.endsWith(".g.dart"))
                return false

            @Suppress("RedundantIf")
            if (virtualFile.name.endsWith(".gr.dart"))
                return false

            return true
        }
    }
}
