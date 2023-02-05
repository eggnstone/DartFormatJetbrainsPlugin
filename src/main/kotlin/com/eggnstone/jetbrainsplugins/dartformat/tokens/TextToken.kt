package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.TokenizerTools

class TextToken2(val text: String) : IToken
{
    override fun equals(other: Any?): Boolean = other is TextToken2 && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "Text(${TokenizerTools.toDisplayString(text)})"
}
