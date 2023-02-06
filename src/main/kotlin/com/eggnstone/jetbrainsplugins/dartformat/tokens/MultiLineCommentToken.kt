package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.TokenizerTools

class MultiLineCommentToken(private val text: String, val isClosed: Boolean = true) : IToken
{
    override fun equals(other: Any?): Boolean = other is MultiLineCommentToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = if (isClosed) "/*$text*/" else "/*$text"

    override fun toString(): String = "MultiLineComment(${TokenizerTools.toDisplayString(text)}${if (isClosed) "" else ", isClosed=false"})"
}
