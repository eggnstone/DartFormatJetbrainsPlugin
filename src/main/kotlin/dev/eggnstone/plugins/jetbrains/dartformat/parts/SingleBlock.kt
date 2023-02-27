package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

class SingleBlock(val header: String, val footer: String, val parts: List<IPart> = listOf()) : IPart
{
    override fun equals(other: Any?): Boolean =
        other is SingleBlock
        && header == other.header
        && footer == other.footer
        && parts == other.parts

    override fun hashCode(): Int = ("$header|$footer|${Tools.partsToDisplayString2(parts)}").hashCode()

    override fun toString(): String = "SingleBlock(${Tools.toDisplayString2(header)}, ${Tools.toDisplayString2(footer)}, ${parts.size} parts)"
}
