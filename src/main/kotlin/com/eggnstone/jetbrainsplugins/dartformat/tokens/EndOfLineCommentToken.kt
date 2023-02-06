package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.TokenizerTools

class EndOfLineCommentToken(private val text: String) : IToken
{
    override fun equals(other: Any?): Boolean = other is EndOfLineCommentToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = "//$text"

    override fun toString(): String = "EndOfLineComment(${TokenizerTools.toDisplayString(text)})"
}
