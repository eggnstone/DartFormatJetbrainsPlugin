package dev.eggnstone.plugins.jetbrains.dartformat.tools

class Logger
{
    companion object
    {
        @Suppress("MemberVisibilityCanBePrivate")
        var isEnabled: Boolean = true

        fun log(s: String)
        {
            if (isEnabled)
                println(s)
        }

        fun logError(s: String)
        {
            if (isEnabled)
                println("ERROR: $s")
        }
    }
}
