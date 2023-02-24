package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.Tools

class ExpressionBlock(val text: String) : IBlock
{
    override fun equals(other: Any?): Boolean = other is ExpressionBlock && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = "Expression(\"${Tools.toDisplayString(text)}\")"
}
