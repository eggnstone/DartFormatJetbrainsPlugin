package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld

class UnknownToken(val text: String) : IToken
{
    override fun equals(other: Any?): Boolean = other is UnknownToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "Unknown(${ToolsOld.toDisplayString2(text)})"
}
