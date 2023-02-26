package dev.eggnstone.plugins.jetbrains.dartformat.blocks

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

class InstructionBlock(val header: String, val footer: String, val blocks: List<IBlock> = listOf()) : IBlock
{
    override fun equals(other: Any?): Boolean =
        other is InstructionBlock
        && header == other.header
        && footer == other.footer
        && blocks == other.blocks

    override fun hashCode(): Int = ("$header|$footer|${Tools.blocksToDisplayString2a(blocks)}").hashCode()

    override fun toString(): String = "Instruction(${Tools.toDisplayString2(header)}, ${Tools.toDisplayString2(footer)}, ${blocks.size} blocks)"
}
