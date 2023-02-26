package com.eggnstone.jetbrainsplugins.dartformat.tokens

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld

class EndOfLineCommentToken(private val text: String) : IToken
{
    init
    {
        if (ToolsOld.containsLineBreak(text))
            throw DartFormatException("EndOfLineCommentToken: text must not contain line breaks: ${ToolsOld.toDisplayString2(text)}")
    }

    override fun equals(other: Any?): Boolean = other is EndOfLineCommentToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = "//$text"

    override fun toString(): String = "EndOfLineComment(\"$text\")"
}
