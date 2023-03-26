package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools

data class MultiBlock(val headers: List<String>, val partLists: List<List<IPart>>, val footer: String) : IPart
{
    override fun recreate(): String
    {
        if (headers.size != partLists.size)
            throw DartFormatException("headers.size != parts.size")

        val result = StringBuilder()

        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until headers.size)
        {
            @Suppress("ReplaceGetOrSet") // dotlin
            result.append(headers.get(i))
            @Suppress("ReplaceGetOrSet") // dotlin
            result.append(recreateParts(partLists.get(i)))
        }

        result.append(footer)

        return result.toString()
    }

    override fun toString(): String
    {
        if (headers.size != partLists.size)
            throw DartFormatException("headers.size != parts.size")

        val result = StringBuilder()

        result.append("MultiBlock(")

        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until headers.size)
        {
            result.append("Header #$i: ")
            @Suppress("ReplaceGetOrSet") // dotlin
            result.append(Tools.toDisplayString(headers.get(i)))
            result.append(", Parts #$i: ")
            @Suppress("ReplaceGetOrSet") // dotlin
            result.append(Tools.toDisplayStringForParts(partLists.get(i)))
            result.append(", ")
        }

        result.append("Footer: ")
        result.append(Tools.toDisplayString(footer))
        result.append(")")

        return result.toString()
    }

    private fun recreateParts(parts: List<IPart>): String
    {
        var result = ""

        for (part in parts)
            result += part.recreate()

        return result
    }
}
