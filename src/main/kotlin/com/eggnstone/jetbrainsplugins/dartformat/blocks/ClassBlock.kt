package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.Tools

class ClassBlock(val header: String, val blocks: List<IBlock>) : IBlock
{
    override fun equals(other: Any?): Boolean = other is ClassBlock && header == other.header && blocks == other.blocks

    override fun hashCode(): Int = ("$header|${Tools.toBlocksDisplayString(blocks)}").hashCode()

    override fun toString(): String = "Class(\"${Tools.toDisplayString(header)}\", [${Tools.toBlocksDisplayString(blocks)}])"
}
