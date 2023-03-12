package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld

class BraceBlock(val blocks: List<IBlock>) : IBlock
{
    override fun equals(other: Any?): Boolean = other is BraceBlock && blocks == other.blocks

    override fun hashCode(): Int = ToolsOld.blocksToDisplayString2(blocks).hashCode()

    override fun toString(): String = "Brace(${ToolsOld.blocksToDisplayString2(blocks)})"
}
