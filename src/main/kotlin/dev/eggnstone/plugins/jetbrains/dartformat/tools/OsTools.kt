package dev.eggnstone.plugins.jetbrains.dartformat.tools

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import java.io.File

class OsTools
{
    companion object
    {
        @Suppress("MemberVisibilityCanBePrivate")
        fun isWindows() = System.getProperty("os.name").lowercase().startsWith("windows")

        fun getExternalDartFormatFilePathOrException(): Any
        {
            Logger.logDebug("OsTools.getExternalDartFormatFilePathOrException()")

            var externalDartFormatFilePath: String?

            if (isWindows())
            {
                Logger.logDebug("  IsWindows: true (" + System.getProperty("os.name") + ")")

                val envPubCache = System.getenv("PUB_CACHE")
                Logger.logDebug("  %PUB_CACHE%:    $envPubCache")
                val envLocalAppData = System.getenv("LOCALAPPDATA")
                Logger.logDebug("  %LOCALAPPDATA%: $envLocalAppData")

                if (envPubCache == null)
                {
                    if (envLocalAppData == null)
                        return DartFormatException.localError(
                            "Cannot find the dart_format package:" +
                                " Neither PUB_CACHE or LOCALAPPDATA environment variable are set."
                        )

                    externalDartFormatFilePath = "$envLocalAppData\\Pub\\Cache"
                }
                else
                    externalDartFormatFilePath = envPubCache

                externalDartFormatFilePath = "$externalDartFormatFilePath\\bin\\dart_format.bat"
                Logger.logDebug("  externalDartFormatFilePath: $externalDartFormatFilePath")
                if (File(externalDartFormatFilePath).exists())
                    return externalDartFormatFilePath
            }
            else
            {
                Logger.logDebug("  IsWindows: false (" + System.getProperty("os.name") + ")")

                externalDartFormatFilePath = "~/.pub-cache/bin/dart_format.sh"
                Logger.logDebug("  externalDartFormatFilePath: $externalDartFormatFilePath")
                if (File(externalDartFormatFilePath).exists())
                    return externalDartFormatFilePath

                externalDartFormatFilePath = "~/.pub-cache/bin/dart_format"
                Logger.logDebug("  externalDartFormatFilePath: $externalDartFormatFilePath")
                if (File(externalDartFormatFilePath).exists())
                    return externalDartFormatFilePath

                externalDartFormatFilePath = "~/.pub-cache/bin/dart_format[.sh]"
            }

            return DartFormatException.localError(
                "Cannot find the dart_format package:" +
                    " File does not exist at expected location: $externalDartFormatFilePath"
            )
        }

        fun getTempDirName(): String
        {
            return if (isWindows())
                System.getenv("TEMP") ?: "C:\\Temp"
            else
                System.getenv("TMPDIR") ?: "/tmp"
        }
    }
}
