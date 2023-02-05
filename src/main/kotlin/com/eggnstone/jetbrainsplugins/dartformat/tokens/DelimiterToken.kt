package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.TokenizerTools

class DelimiterToken(val text: String) : IToken
{
    companion object
    {
        val CLOSING_BRACKET = DelimiterToken(")")
        val COMMA = DelimiterToken(",")
    }

    override fun equals(other: Any?): Boolean = other is DelimiterToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "Delimiter(${TokenizerTools.toDisplayString(text)})"
}
