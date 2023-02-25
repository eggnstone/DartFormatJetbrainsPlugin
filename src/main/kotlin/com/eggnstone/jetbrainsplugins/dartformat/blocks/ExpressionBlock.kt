package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld

class ExpressionBlock(val text: String) : IBlock
{
    override fun equals(other: Any?): Boolean = other is ExpressionBlock && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = "Expression(${ToolsOld.toDisplayString2(text)})"
}
