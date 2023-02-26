package dev.eggnstone.plugins.jetbrains.dartformat.simple_blocks

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

interface IPart

class TextPart(val text: String) : IPart

class MetaPart(val parts: List<IPart>) : IPart

class SimpleInstructionBlock2(val header: String, val footer: String, val parts: List<IPart> = listOf()) : ISimpleBlock
{
    override fun equals(other: Any?): Boolean =
        other is SimpleInstructionBlock2
        && header == other.header
        && footer == other.footer
        && parts == other.parts

    override fun hashCode(): Int = ("$header|$footer|${Tools.partsToDisplayString2(parts)}").hashCode()

    override fun toString(): String = "SimpleInstruction2(${Tools.toDisplayString2(header)}, ${Tools.toDisplayString2(footer)}, ${parts.size} parts)"
}
