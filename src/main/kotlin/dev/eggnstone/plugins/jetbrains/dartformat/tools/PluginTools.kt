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

            if (virtualFile.name.endsWith(".freezed.dart")
                || virtualFile.name.endsWith(".g.dart")
                || virtualFile.name.endsWith(".gr.dart")
                || virtualFile.name.endsWith(".pb.dart"))
                return false

            return true
        }
    }
}
