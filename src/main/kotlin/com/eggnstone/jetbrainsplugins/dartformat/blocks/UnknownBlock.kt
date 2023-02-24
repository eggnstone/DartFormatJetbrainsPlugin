package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.Tools

class UnknownBlock(val text: String) : IBlock
{
    override fun equals(other: Any?): Boolean = other is UnknownBlock && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = "Unknown(\"${Tools.toDisplayString(text)}\")"
}
