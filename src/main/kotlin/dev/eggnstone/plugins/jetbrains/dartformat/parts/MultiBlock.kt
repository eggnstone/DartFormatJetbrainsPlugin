package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

data class MultiBlock(val headers: List<String>, val partLists: List<List<IPart>>, val footer: String) : IPart
{
    companion object
    {
        fun single(header: String, footer: String, parts: List<IPart> = listOf()): IPart = MultiBlock(listOf(header), listOf(parts), footer)

        fun double(header: String, middle: String, footer: String, parts1: List<IPart> = listOf(), parts2: List<IPart> = listOf()): IPart = MultiBlock(listOf(header, middle), listOf(parts1, parts2), footer)
    }

    init
    {
        if (headers.size != partLists.size)
            throw DartFormatException("headers.size (${headers.size}) != partLists.size (${partLists.size})")
    }

    override fun recreate(): String
    {
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
        if (DotlinTools.isEmpty(headers))
            throw DartFormatException("DotlinTools.isEmpty(headers)")

        val result = StringBuilder()

        when (headers.size)
        {
            1 -> result.append("MultiBlock.single(")
            2 -> result.append("MultiBlock.double(")
            else -> result.append("MultiBlock(")
        }

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
