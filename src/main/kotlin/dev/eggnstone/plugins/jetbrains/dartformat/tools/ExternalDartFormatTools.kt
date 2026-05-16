package dev.eggnstone.plugins.jetbrains.dartformat.tools

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.process.ProcessBuilderInfo
import dev.eggnstone.plugins.jetbrains.dartformat.process.ProcessInfoOrLocalError
import java.io.File

class ExternalDartFormatTools
{
    companion object
    {
        fun getExternalDartFormatInfo(): ProcessInfoOrLocalError
        {
            val externalDartFormatExecutable: String?

            if (OsTools.instance.isWindows)
            {
                val externalDartFormatPath: String
                if (OsTools.instance.envPubCache == null)
                {
                    if (OsTools.instance.envLocalAppData == null)
                        return ProcessInfoOrLocalError.LocalError(
                            DartFormatException.localError(
                                "Cannot find the dart_format package:" +
                                    " Neither PUB_CACHE nor LOCALAPPDATA environment variable are set."
                            )
                        )

                    externalDartFormatPath = "${OsTools.instance.envLocalAppData}\\Pub\\Cache\\bin"
                }
                else
                    externalDartFormatPath = "${OsTools.instance.envPubCache}\\bin"

                externalDartFormatExecutable = "$externalDartFormatPath\\dart_format.bat"
            }
            else
            {
                if (OsTools.instance.envShell.isEmpty())
                    return ProcessInfoOrLocalError.LocalError(
                        DartFormatException.localError(
                            "Cannot execute dart: SHELL environment variable is not set."
                        )
                    )

                if (OsTools.instance.envHome.isNullOrEmpty())
                    return ProcessInfoOrLocalError.LocalError(
                        DartFormatException.localError(
                            "Cannot execute dart_format: HOME environment variable is not set."
                        )
                    )

                val externalDartFormatPath = "${OsTools.instance.envHome}/.pub-cache/bin"
                externalDartFormatExecutable = "$externalDartFormatPath/dart_format"
            }

            if (File(externalDartFormatExecutable).exists())
            {
                val command = "\"$externalDartFormatExecutable\" --web --errors-as-json --log-to-temp-file=true"
                return ProcessInfoOrLocalError.Normal(ProcessBuilderInfo(OsTools.instance.envShell, OsTools.instance.envShellParam, command))
            }

            return ProcessInfoOrLocalError.LocalError(
                DartFormatException.localError(
                    "Cannot find the dart_format package: File does not exist at expected location: $externalDartFormatExecutable"
                )
            )
        }

        fun getInstallExternalDartFormatInfo(): ProcessInfoOrLocalError
        {
            val dartExecutable: String
            if (OsTools.instance.isWindows)
            {
                dartExecutable = "dart.bat"
            }
            else
            {
                if (OsTools.instance.envShell.isEmpty())
                    return ProcessInfoOrLocalError.LocalError(
                        DartFormatException.localError(
                            "Cannot execute dart: SHELL environment variable is not set."
                        )
                    )

                dartExecutable = "dart"
            }

            //val command = "$dartExecutable"+"x pub global activate dart_format"
            val command = "$dartExecutable pub global activate dart_format"
            return ProcessInfoOrLocalError.Normal(ProcessBuilderInfo(OsTools.instance.envShell, OsTools.instance.envShellParam, command))
        }
    }
}
