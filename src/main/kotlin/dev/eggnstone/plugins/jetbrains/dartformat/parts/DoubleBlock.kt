package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

data class DoubleBlock(val header: String, val middle: String, val footer: String, val parts1: List<IPart> = listOf(), val parts2: List<IPart> = listOf()) : IPart
{
    override fun recreate(): String = header + recreateParts(parts1) + middle + recreateParts(parts2) + footer

    override fun toString(): String =
        "DoubleBlock(" +
            "header: ${Tools.toDisplayString(header)}, " +
            "parts1: ${Tools.toDisplayStringForParts(parts1)}), " +
            "middle: ${Tools.toDisplayString(middle)}, " +
            "parts2: ${Tools.toDisplayStringForParts(parts2)}, " +
            "footer: ${Tools.toDisplayString(footer)})"

    private fun recreateParts(parts: List<IPart>): String
    {
        var result = ""

        for (part in parts)
            result += part.recreate()

        return result
    }
}
