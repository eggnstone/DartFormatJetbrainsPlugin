package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

class LineSplitter
{
    fun split(s: String): List<String>
    {
        DotlinLogger.log("LineSplitter.split(${Tools.toDisplayString(s)})")

        if (DotlinTools.isEmpty(s))
            return listOf()

        if (DotlinTools.containsString(s, "\r\n"))
            return split(s, "\r\n")

        if (DotlinTools.containsString(s, "\n\r"))
            return split(s, "\n\r")

        if (DotlinTools.containsChar(s, "\n"))
            return split(s, "\n")

        if (DotlinTools.containsChar(s, "\r"))
            return split(s, "\r")

        return listOf(s)
    }

    private fun split(s: String, delimiter: String): List<String>
    {
        DotlinLogger.log("LineSplitter.split(${Tools.toDisplayString(s)},${Tools.toDisplayString(delimiter)})")

        val outputLines = mutableListOf<String>()

        val lines = DotlinTools.split(s, delimiter)
        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (index in 0 until lines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val line = lines.get(index) // workaround for dotlin
            outputLines.add(if (index < lines.size - 1) line + delimiter else line)
        }

        DotlinLogger.log(Tools.toDisplayString(s) + " '${Tools.toDisplayString(delimiter)}'")
        DotlinLogger.log("  -> " + Tools.toDisplayStringForStrings(lines))
        DotlinLogger.log("  -> " + Tools.toDisplayStringForStrings(outputLines))

        return outputLines
    }
}
