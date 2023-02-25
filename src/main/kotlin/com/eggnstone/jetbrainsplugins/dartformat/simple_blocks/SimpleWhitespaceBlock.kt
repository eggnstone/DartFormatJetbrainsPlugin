package com.eggnstone.jetbrainsplugins.dartformat.simple_blocks

import com.eggnstone.jetbrainsplugins.dartformat.Tools

class SimpleWhitespaceBlock(val text: String) : ISimpleBlock
{
    override fun equals(other: Any?): Boolean = other is SimpleWhitespaceBlock && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = "SimpleWhitespace(${Tools.toDisplayString2(text)})"
}
