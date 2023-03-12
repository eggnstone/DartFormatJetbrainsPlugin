package dev.eggnstone.plugins.jetbrains.dartformat.dotlin

class DotlinLogger
{
    companion object
    {
        var isEnabled: Boolean = true

        fun log(s: String)
        {
            if (isEnabled)
                println(s)
        }
    }
}
