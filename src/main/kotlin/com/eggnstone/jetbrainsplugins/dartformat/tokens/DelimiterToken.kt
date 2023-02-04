package com.eggnstone.jetbrainsplugins.dartformat.tokens

class DelimiterToken(val delimiter: String) : IToken
{
    companion object
    {
        val CLOSING_BRACKET = DelimiterToken(")")
        val COMMA = DelimiterToken(",")
    }

    val isNewline: Boolean = delimiter == "\n" || delimiter == "\n\r" || delimiter == "\r" || delimiter == "\r\n"

    override fun equals(other: Any?): Boolean
    {
        return other is DelimiterToken && delimiter == other.delimiter
    }

    override fun recreate(): String
    {
        return delimiter
    }

    override fun toString(): String
    {
        if (delimiter == "\n")
            return "\\n"

        if (delimiter == "\n\r")
            return "\\n\\r"

        if (delimiter == "\r")
            return "\\r"

        if (delimiter == "\r\n")
            return "\\r\\n"

        return delimiter
    }

    override fun hashCode(): Int
    {
        return delimiter.hashCode()
    }
}
