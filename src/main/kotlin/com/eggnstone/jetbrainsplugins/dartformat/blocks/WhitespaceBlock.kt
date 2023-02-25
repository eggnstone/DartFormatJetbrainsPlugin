package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld

class WhitespaceBlock(val text: String) : IBlock
{
    override fun equals(other: Any?): Boolean = other is WhitespaceBlock && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = "Whitespace(${ToolsOld.toDisplayString2(text)})"
}
