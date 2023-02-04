package com.eggnstone.jetbrainsplugins.dartformat.tokens

class TextToken(val text: String) : IToken
{
    override fun recreate(): String
    {
        return toString()
    }

    override fun toString(): String
    {
        return text
    }

    override fun equals(other: Any?): Boolean
    {
        return other is TextToken && text == other.text
    }

    override fun hashCode(): Int
    {
        return text.hashCode()
    }
}
