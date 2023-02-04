package com.eggnstone.jetbrainsplugins.dartformat.tokens

class DelimiterToken(val delimiter: String) : IToken
{
    companion object
    {
        val COMMA = DelimiterToken(",")
        val CLOSING_BRACKET = DelimiterToken(")")
    }

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

        return delimiter
    }

    override fun hashCode(): Int
    {
        return delimiter.hashCode()
    }
}
