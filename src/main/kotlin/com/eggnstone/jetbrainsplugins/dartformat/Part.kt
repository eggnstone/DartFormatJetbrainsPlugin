package com.eggnstone.jetbrainsplugins.dartformat

class Part(val text: String, var delimiter: Char?)
{
    fun recreate(): String
    {
        if (delimiter == null)
            return text

        return text + delimiter
    }

    override fun toString(): String
    {
        if (delimiter == null)
            return "Delimiter: NONE Text: \"$text\""

        var delim = delimiter.toString()
        if (delim == "\n")
            delim = "\\n"

        return "Delimiter: \"$delim\" Text: \"$text\""
    }
}
