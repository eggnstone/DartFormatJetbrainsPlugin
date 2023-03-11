package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

data class DoubleBlock(val header: String, val middle: String, val footer: String, val parts1: List<IPart> = listOf(), val parts2: List<IPart> = listOf()) : IPart
{
    /*override fun equals(other: Any?): Boolean =
        other is DoubleBlock
        && header == other.header
        && middle == other.middle
        && footer == other.footer
        && parts1 == other.parts1
        && parts2 == other.parts2

    override fun hashCode(): Int = ("$header|$footer|${Tools.toDisplayStringForParts(parts1)}|${Tools.toDisplayStringForParts(parts2)}").hashCode()*/

    override fun recreate(): String = header + recreateParts(parts1) + middle + recreateParts(parts2) + footer

    override fun toString(): String = "DoubleBlock(${Tools.toDisplayString(header)}" +
    ", ${Tools.toDisplayString(middle)}" +
    ", ${Tools.toDisplayString(footer)}" +
    ", parts1: ${Tools.toDisplayStringForParts(parts1)})" +
    ", ${Tools.toDisplayString(footer)}" +
    ", parts2: ${Tools.toDisplayStringForParts(parts2)})"

    private fun recreateParts(parts: List<IPart>): String
    {
        var result = ""

        for (part in parts)
            result += part.recreate()

        return result
    }
}
