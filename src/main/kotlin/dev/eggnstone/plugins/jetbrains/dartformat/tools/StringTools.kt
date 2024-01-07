package dev.eggnstone.plugins.jetbrains.dartformat.tools

class StringTools
{
    companion object
    {
        fun toDisplayString(s: String): String
        {
            return "\"${s.replace("\n", "\\n").replace("\r", "\\r")}\""
        }
    }
}