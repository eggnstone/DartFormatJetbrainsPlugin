package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.Tools

class CurlyBracketsBlock(val text: String) : IBlock
{
    override fun equals(other: Any?): Boolean = other is CurlyBracketsBlock && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = "CurlyBrackets(\"${Tools.toDisplayString(text)}\")"
}
