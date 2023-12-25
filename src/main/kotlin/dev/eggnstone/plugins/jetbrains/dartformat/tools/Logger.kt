package dev.eggnstone.plugins.jetbrains.dartformat.tools

class Logger
{
    companion object
    {
        var isEnabled: Boolean = true
        //var isEnabled: Boolean = false

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
