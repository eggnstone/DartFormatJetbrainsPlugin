package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.Tools

class CurlyBracketBlock(val blocks: List<IBlock>) : IBlock
{
    override fun equals(other: Any?): Boolean = other is CurlyBracketBlock && blocks == other.blocks

    override fun hashCode(): Int = Tools.toBlocksDisplayString(blocks).hashCode()

    override fun toString(): String = "CurlyBracket([${Tools.toBlocksDisplayString(blocks)}])"
}
