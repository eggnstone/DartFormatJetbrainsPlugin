package dev.eggnstone.plugins.jetbrains.dartformat.simple_blocks

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

class SimpleInstructionBlock(val text: String) : ISimpleBlock
{
    override fun equals(other: Any?): Boolean = other is SimpleInstructionBlock && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = "SimpleInstruction(${Tools.toDisplayString2(text)})"
}