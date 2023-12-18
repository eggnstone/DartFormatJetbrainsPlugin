package dev.eggnstone.plugins.jetbrains.dartformat.tools

class OsTools
{
    companion object
    {
        fun isWindows(): Boolean
        {
            val osName = System.getProperty("os.name").lowercase()
            return osName.startsWith("windows")
        }
    }
}
