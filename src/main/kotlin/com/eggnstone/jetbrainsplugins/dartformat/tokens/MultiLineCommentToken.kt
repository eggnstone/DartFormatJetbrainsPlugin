package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.TokenizerTools

class MultiLineCommentToken(private val text: String) : IToken
{
    override fun equals(other: Any?): Boolean = other is MultiLineCommentToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = "/*$text*/"

    override fun toString(): String = "MultiLineComment(${TokenizerTools.toDisplayString(text)})"
}
