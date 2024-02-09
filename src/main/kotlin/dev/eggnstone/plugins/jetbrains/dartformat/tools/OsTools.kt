package dev.eggnstone.plugins.jetbrains.dartformat.tools

class OsTools
{
    companion object
    {
        fun isWindows() = System.getProperty("os.name").lowercase().startsWith("windows")
    }
}
