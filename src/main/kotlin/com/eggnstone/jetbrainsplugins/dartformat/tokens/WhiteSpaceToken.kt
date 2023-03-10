package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld

class WhiteSpaceToken(val text: String) : IToken
{
    override fun equals(other: Any?): Boolean = other is WhiteSpaceToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "WhiteSpace(${ToolsOld.toDisplayString2(text)})"
}
