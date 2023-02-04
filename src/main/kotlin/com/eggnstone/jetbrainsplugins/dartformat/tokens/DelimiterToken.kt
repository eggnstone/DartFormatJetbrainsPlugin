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
        if (other !is DelimiterToken)
            return false

        return delimiter == other.delimiter
    }

    override fun toString(): String
    {
        return delimiter
    }
}
