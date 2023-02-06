package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.Tools

class EndOfLineCommentToken : IToken
{
    private val text: String

    constructor(text: String)
    {
        if (Tools.containsLineBreak(text))
            throw Exception("containsLineBreak: \"${Tools.toDisplayString(text)}\"")

        this.text = text
    }

    override fun equals(other: Any?): Boolean = other is EndOfLineCommentToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = "//$text"

    override fun toString(): String = "EndOfLineComment($text)"
}
