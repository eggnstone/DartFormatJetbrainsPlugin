package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.Tools

class SimpleInstructionBlock(val text: String) : ISimpleBlock
{
    override fun equals(other: Any?): Boolean = other is SimpleInstructionBlock && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = "SimpleInstruction(${Tools.toDisplayString2(text)})"
}
