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

            // TODO: paths starting with "."
            if (virtualFile.path.contains("/.dart_tool/") || virtualFile.path.contains("\\.dart_tool\\"))
                return false

            if (virtualFile.name.endsWith(".freezed.dart")
                || virtualFile.name.endsWith(".g.dart")
                || virtualFile.name.endsWith(".gr.dart")
                || virtualFile.name.endsWith(".pb.dart")
                || virtualFile.name.endsWith(".pbenum.dart")
                || virtualFile.name.endsWith(".pbjson.dart")
                || virtualFile.name.endsWith(".pbserver.dart")
            )
                return false

            return true
        }

        fun isNonDartFile(virtualFile: VirtualFile): Boolean = !virtualFile.isDirectory && virtualFile.extension != "dart"
    }
}
