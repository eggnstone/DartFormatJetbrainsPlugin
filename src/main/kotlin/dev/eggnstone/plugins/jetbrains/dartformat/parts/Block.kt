package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

class Block(val header: String, val footer: String, val parts: List<IPart> = listOf()) : IPart
{
    override fun equals(other: Any?): Boolean =
        other is Block
        && header == other.header
        && footer == other.footer
        && parts == other.parts

    override fun hashCode(): Int = ("$header|$footer|${Tools.partsToDisplayString2(parts)}").hashCode()

    override fun toString(): String = "Block(${Tools.toDisplayString2(header)}, ${Tools.toDisplayString2(footer)}, ${parts.size} parts)"
}
