package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld

class ClassBlock(val header: String, val blocks: List<IBlock>) : IBlock
{
    override fun equals(other: Any?): Boolean = other is ClassBlock && header == other.header && blocks == other.blocks

    override fun hashCode(): Int = ("$header|${ToolsOld.blocksToDisplayString2(blocks)}").hashCode()

    override fun toString(): String = "Class(${ToolsOld.toDisplayString2(header)}, ${ToolsOld.blocksToDisplayString2(blocks)})"
}
