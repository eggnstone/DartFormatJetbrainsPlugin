package dev.eggnstone.plugins.jetbrains.dartformat.tools

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import java.io.File

class OsTools
{
    companion object
    {
        @Suppress("MemberVisibilityCanBePrivate")
        fun isWindows() = System.getProperty("os.name").lowercase().startsWith("windows")

        fun getExternalDartFormatFilePathOrException(): ExternalDartFormatInfo
        {
            Logger.logDebug("OsTools.getExternalDartFormatFilePathOrException()")

            Logger.logDebug("  IsWindows:                         " + isWindows() + " (" + System.getProperty("os.name") + ")")
            Logger.logDebug("  System.getProperty(java.io.tmpdir) " + System.getProperty("java.io.tmpdir"))

            if (ProcessHandle.current().info().command().isPresent)
                Logger.logDebug("  ProcessHandle.command:             " + ProcessHandle.current().info().command().get())

            if (ProcessHandle.current().parent().isPresent && ProcessHandle.current().parent().get().info().command().isPresent)
                Logger.logDebug("  ProcessHandle.parent.command:      " + ProcessHandle.current().parent().get().info().command().get())

            var externalDartFormatFilePath: String?

            if (isWindows())
            {
                val envPubCache = System.getenv("PUB_CACHE")
                val envLocalAppData = System.getenv("LOCALAPPDATA")
                Logger.logDebug("  %PUB_CACHE%:                       $envPubCache")
                Logger.logDebug("  %LOCALAPPDATA%:                    $envLocalAppData")

                if (envPubCache == null)
                {
                    if (envLocalAppData == null)
                        return ExternalDartFormatInfo.exception(
                            DartFormatException.localError(
                                "Cannot find the dart_format package:" +
                                    " Neither PUB_CACHE nor LOCALAPPDATA environment variable are set."
                            )
                        )

                    externalDartFormatFilePath = "$envLocalAppData\\Pub\\Cache"
                }
                else
                    externalDartFormatFilePath = envPubCache

                externalDartFormatFilePath = "$externalDartFormatFilePath\\bin\\dart_format.bat"
                if (File(externalDartFormatFilePath).exists())
                    return ExternalDartFormatInfo.normal(externalDartFormatFilePath)
            }
            else
            {
                var shell: String? = null
                if (ProcessHandle.current().parent().isPresent && ProcessHandle.current().parent().get().info().command().isPresent)
                {
                    val parentCommand = ProcessHandle.current().parent().get().info().command().get()
                    if (parentCommand.endsWith("/bash"))
                        shell = parentCommand
                    else if (parentCommand.endsWith("/sh"))
                        shell = parentCommand
                    else if (parentCommand.endsWith("/zsh"))
                        shell = parentCommand
                }

                Logger.logDebug("  Shell:                             " + (shell ?: "<none>"))

                val home = System.getProperty("user.home")
                Logger.logDebug("  System.getProperty(user.home):     $home")

                externalDartFormatFilePath = "$home/.pub-cache/bin/dart_format"
                if (File(externalDartFormatFilePath).exists())
                    return if (shell == null)
                        ExternalDartFormatInfo.normal(externalDartFormatFilePath)
                    else
                        ExternalDartFormatInfo.withAdditionalParam(shell, externalDartFormatFilePath)

                externalDartFormatFilePath = "$home/.pub-cache/bin/dart_format.sh"
                if (File(externalDartFormatFilePath).exists())
                    return if (shell == null)
                        ExternalDartFormatInfo.normal(externalDartFormatFilePath)
                    else
                        ExternalDartFormatInfo.withAdditionalParam(shell, externalDartFormatFilePath)

                externalDartFormatFilePath = "~/.pub-cache/bin/dart_format[.sh]"
            }

            return ExternalDartFormatInfo.exception(
                DartFormatException.localError(
                    "Cannot find the dart_format package:" +
                        " File does not exist at expected location: $externalDartFormatFilePath"
                )
            )
        }

        fun getTempDirName(): String
        {
            return System.getProperty("java.io.tmpdir")
        }
    }
}
