package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.Tools

class CurlyBracketBlock(val blocks: List<IBlock>) : IBlock
{
    override fun equals(other: Any?): Boolean = other is CurlyBracketBlock && blocks == other.blocks

    override fun hashCode(): Int = Tools.blocksToDisplayString2(blocks).hashCode()

    override fun toString(): String = "CurlyBracket(${Tools.blocksToDisplayString2(blocks)})"
}
