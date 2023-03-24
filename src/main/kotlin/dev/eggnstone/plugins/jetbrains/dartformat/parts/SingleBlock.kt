package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

data class SingleBlock(val header: String, val footer: String, val parts: List<IPart> = listOf()) : IPart
{
    override fun recreate(): String = header + recreateParts() + footer

    override fun toString(): String =
        "SingleBlock(${Tools.toDisplayString(header)}" +
            ", ${Tools.toDisplayString(footer)}" +
            ", parts: ${Tools.toDisplayStringForParts(parts)})"

    private fun recreateParts(): String
    {
        var result = ""

        for (part in parts)
            result += part.recreate()

        return result
    }
}
