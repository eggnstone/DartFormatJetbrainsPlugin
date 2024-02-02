package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.openapi.vfs.VirtualFile

class PluginTools
{
    companion object
    {
        fun isDartFile(virtualFile: VirtualFile): Boolean
        {
            if (virtualFile.extension != "dart")
                return false

            if (virtualFile.path.contains("/.dart_tool/") || virtualFile.path.contains("\\.dart_tool\\"))
                return false

            if (virtualFile.name.endsWith(".freezed.dart"))
                return false

            if (virtualFile.name.endsWith(".g.dart"))
                return false

            if (virtualFile.name.endsWith(".gr.dart"))
                return false

            return true
        }
    }
}
