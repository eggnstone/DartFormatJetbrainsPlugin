package dev.eggnstone.plugins.jetbrains.dartformat.blocks

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

class BlockInstructionBlock(val header: String, val footer: String, val blocks: List<IBlock> = listOf()) : IBlock
{
    override fun equals(other: Any?): Boolean =
        other is BlockInstructionBlock
        && header == other.header
        && footer == other.footer
        && blocks == other.blocks

    override fun hashCode(): Int = ("$header|$footer|${Tools.blocksToDisplayString2a(blocks)}").hashCode()

    override fun toString(): String = "BlockInstruction(${Tools.toDisplayString2(header)}, ${Tools.toDisplayString2(footer)}, ${blocks.size} blocks)"
}
