package com.eggnstone.jetbrainsplugins.dartformat.tokens

class TextToken(val text: String) : IToken
{
    override fun equals(other: Any?): Boolean = other is TextToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = toString()

    override fun toString(): String = text
}
