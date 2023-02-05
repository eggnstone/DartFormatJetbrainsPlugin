package com.eggnstone.jetbrainsplugins.dartformat.tokens

class DelimiterToken(val delimiter: String) : IToken
{
    companion object
    {
        val CLOSING_BRACKET = DelimiterToken(")")
        val COMMA = DelimiterToken(",")
    }

    override fun equals(other: Any?): Boolean = other is DelimiterToken && delimiter == other.delimiter

    override fun hashCode(): Int = delimiter.hashCode()

    override fun recreate(): String = delimiter

    override fun toString(): String = delimiter
}
