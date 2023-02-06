package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.Tools

class LineBreakToken constructor(val text: String) : IToken
{
    companion object
    {
        val N = LineBreakToken("\n")
        val NR = LineBreakToken("\n\r")
        val R = LineBreakToken("\r")
        val RN = LineBreakToken("\r\n")
    }

    override fun equals(other: Any?): Boolean = other is LineBreakToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "LineBreak(${Tools.toDisplayString(text)})"
}
