package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

class LineSplitter
{
    fun split(s: String): List<String>
    {
        //DotlinLogger.log("LineSplitter.split(${Tools.toDisplayString(s)})")

        if (DotlinTools.isEmpty(s))
            return listOf()

        if (DotlinTools.containsString(s, "\n\r"))
            return split(s, "\n\r")

        if (DotlinTools.containsString(s, "\r\n"))
            return split(s, "\r\n")

        if (DotlinTools.containsChar(s, "\n"))
            return split(s, "\n")

        if (DotlinTools.containsChar(s, "\r"))
            return split(s, "\r")

        return listOf(s)
    }

    private fun split(s: String, delimiter: String): List<String>
    {
        //DotlinLogger.log("  LineSplitter.split(${Tools.toDisplayString(s)},${Tools.toDisplayString(delimiter)})")

        if (s == delimiter)
            return listOf(delimiter)

        val outputLines = mutableListOf<String>()

        var currentText = ""
        val lines = StringSplitter.split(s, delimiter, trim = true)
        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until lines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val line = lines.get(i) // workaround for dotlin
            @Suppress("ReplaceSizeZeroCheckWithIsEmpty") // workaround for dotlin
            @Suppress("LiftReturnOrAssignment")
            if (line.length == 0)
            {
                outputLines.add(currentText + delimiter)
                currentText = ""
            }
            else
                currentText = line
        }

        if (DotlinTools.isNotEmpty(currentText))
            outputLines.add(currentText)

        //DotlinLogger.log("    Split ${Tools.toDisplayString(s)} by ${Tools.toDisplayString(delimiter)}")
        //DotlinLogger.log("      -> " + Tools.toDisplayStringForStrings(lines))
        //DotlinLogger.log("      -> " + Tools.toDisplayStringForStrings(outputLines))

        return outputLines
    }
}
