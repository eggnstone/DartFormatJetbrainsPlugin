package dev.eggnstone.plugins.jetbrains.dartformat.tools

class OsTools
{
    val envHome: String?
    val envLocalAppData: String?
    val envPubCache: String?
    val envShell: String
    val envShellParam: String
    val isWindows: Boolean
    val osName: String

    companion object
    {
        val instance: OsTools = OsTools()

        fun getTempDirName(): String
        {
            return System.getProperty("java.io.tmpdir")
        }
    }

    init
    {
        Logger.logDebug("OsTools()")

        osName = System.getProperty("os.name")
        isWindows = osName.lowercase().startsWith("windows")
        Logger.logDebug("  IsWindows:      " + isWindows + " (" + osName + ")")

        Logger.logDebug("  TempDir:        ${getTempDirName()}")

        if (isWindows)
        {
            envLocalAppData = System.getenv("LOCALAPPDATA")
            Logger.logDebug("  %LOCALAPPDATA%: $envLocalAppData")

            envPubCache = System.getenv("PUB_CACHE")
            Logger.logDebug("  %PUB_CACHE%:    $envPubCache")

            envHome = null
            envShell = "cmd"
            envShellParam = "/c"
        }
        else
        {
            envHome = System.getenv("HOME")
            Logger.logDebug("  \$HOME:          $envHome")

            envShell = System.getenv("SHELL")
            Logger.logDebug("  \$SHELL:         $envShell")

            envLocalAppData = null
            envPubCache = null
            envShellParam = "-c"
        }
    }
}
