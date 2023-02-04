package com.eggnstone.jetbrainsplugins.dartformat.tokens

class MultiLineCommentToken(private val text: String) : IToken
{
    override fun recreate(): String
    {
        return "/*$text*/"
    }

    override fun equals(other: Any?): Boolean
    {
        return other is MultiLineCommentToken && text == other.text
    }

    override fun hashCode(): Int
    {
        return text.hashCode()
    }

    override fun toString(): String
    {
        return text
    }
}
