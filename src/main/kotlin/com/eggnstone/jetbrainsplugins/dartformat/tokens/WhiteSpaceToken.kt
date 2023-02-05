package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.TokenizerTools

class WhiteSpaceToken(val text: String) : IToken
{
    //val isNewLine: Boolean = delimiter == "\n" || delimiter == "\n\r" || delimiter == "\r" || delimiter == "\r\n"

    override fun equals(other: Any?): Boolean = other is WhiteSpaceToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "WhiteSpace(${TokenizerTools.toDisplayString(text)})"
}
