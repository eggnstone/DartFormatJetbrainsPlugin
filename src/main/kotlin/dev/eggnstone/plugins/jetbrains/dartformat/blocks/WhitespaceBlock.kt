package dev.eggnstone.plugins.jetbrains.dartformat.blocks

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

class WhitespaceBlock(val text: String) : IBlock
{
    override fun equals(other: Any?): Boolean = other is WhitespaceBlock && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = "Whitespace(${Tools.toDisplayString2(text)})"
}