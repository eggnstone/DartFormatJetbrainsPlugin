package com.eggnstone.jetbrainsplugins.dartformat.tokens

class KeywordToken(val text: String) : IToken
{
    override fun equals(other: Any?): Boolean = other is KeywordToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "Keyword(\"$text\")"
}
