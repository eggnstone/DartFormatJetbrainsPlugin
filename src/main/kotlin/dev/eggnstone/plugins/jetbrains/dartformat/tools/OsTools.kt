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
                Logger.logDebug("  System.getProperty(java.io.tmpdir) " + System.getProperty("java.io.tmpdir"))

                val envPubCache = System.getenv("PUB_CACHE")
                val envLocalAppData = System.getenv("LOCALAPPDATA")
                Logger.logDebug("  %PUB_CACHE%:                       $envPubCache")
                Logger.logDebug("  %LOCALAPPDATA%:                    $envLocalAppData")

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
                //Logger.logDebug("  externalDartFormatFilePath: $externalDartFormatFilePath")
                if (File(externalDartFormatFilePath).exists())
                    return externalDartFormatFilePath
            }
            else
            {
                Logger.logDebug("  IsWindows: false (" + System.getProperty("os.name") + ")")
                Logger.logDebug("  System.getProperty(java.io.tmpdir)  " + System.getProperty("java.io.tmpdir"))

                val home = System.getProperty("user.home")
                Logger.logDebug("  System.getProperty(user.home):      $home")
                Logger.logDebug("  home/.pub-cache/bin/dart_format:    " + File("$home/.pub-cache/bin/dart_format").exists())
                Logger.logDebug("  home/.pub-cache/bin/dart_format.sh: " + File("$home/.pub-cache/bin/dart_format.sh").exists())

                externalDartFormatFilePath = "$home/.pub-cache/bin/dart_format.sh"
                //Logger.logDebug("  externalDartFormatFilePath: $externalDartFormatFilePath")
                if (File(externalDartFormatFilePath).exists())
                    return externalDartFormatFilePath

                externalDartFormatFilePath = "$home/.pub-cache/bin/dart_format"
                //Logger.logDebug("  externalDartFormatFilePath: $externalDartFormatFilePath")
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
            return System.getProperty("java.io.tmpdir")
        }
    }
}
