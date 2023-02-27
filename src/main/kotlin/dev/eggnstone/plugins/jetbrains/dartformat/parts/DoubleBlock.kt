package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

class DoubleBlock(val header: String, val middle: String, val footer: String, val parts1: List<IPart> = listOf(), val parts2: List<IPart> = listOf()) : IPart
{
    override fun equals(other: Any?): Boolean =
        other is DoubleBlock
        && header == other.header
        && middle == other.middle
        && footer == other.footer
        && parts1 == other.parts1
        && parts2 == other.parts2

    override fun hashCode(): Int = ("$header|$footer|${Tools.partsToDisplayString2(parts1)}|${Tools.partsToDisplayString2(parts2)}").hashCode()

    override fun toString(): String = "DoubleBlock(${Tools.toDisplayString2(header)}" +
    ", ${Tools.toDisplayString2(middle)}" +
    ", ${Tools.toDisplayString2(footer)}" +
    ", parts1: ${Tools.partsToDisplayString2(parts1)})" +
    ", ${Tools.toDisplayString2(footer)}" +
    ", parts2: ${Tools.partsToDisplayString2(parts2)})"
}
