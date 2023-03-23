package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper

class LineSplitter
{
    fun split(s: String, trim: Boolean): List<String>
    {
        //if (DotlinLogger.isEnabled) DotlinLogger.log("LineSplitter.split(${Tools.toDisplayString(s)})")

        if (StringWrapper.isEmpty(s))
            return listOf()

        val nrPos = StringWrapper.indexOf(s, "\n\r")
        val rnPos = StringWrapper.indexOf(s, "\r\n")

        if (nrPos >= 0 && (rnPos < 0 || nrPos < rnPos))
            return split(s, "\n\r", trim)

        if (rnPos >= 0 && (nrPos < 0 || rnPos < nrPos))
            return split(s, "\r\n", trim)

        if (StringWrapper.containsChar(s, "\n"))
            return split(s, "\n", trim)

        if (StringWrapper.containsChar(s, "\r"))
            return split(s, "\r", trim)

        return listOf(s)
    }

    private fun split(s: String, delimiter: String, trim: Boolean): List<String>
    {
        //if (DotlinLogger.isEnabled) DotlinLogger.log("  LineSplitter.split(${Tools.toDisplayString(s)},${Tools.toDisplayString(delimiter)})")

        if (s == delimiter)
            return listOf(delimiter)

        val outputLines = mutableListOf<String>()

        var currentText = ""
        val lines = StringSplitter.split(s, delimiter, trim)
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

        if (StringWrapper.isNotEmpty(currentText))
            outputLines.add(currentText)

        //if (DotlinLogger.isEnabled) DotlinLogger.log("    Split ${Tools.toDisplayString(s)} by ${Tools.toDisplayString(delimiter)}")
        //if (DotlinLogger.isEnabled) DotlinLogger.log("      -> " + Tools.toDisplayStringForStrings(lines))
        //if (DotlinLogger.isEnabled) DotlinLogger.log("      -> " + Tools.toDisplayStringForStrings(outputLines))

        return outputLines
    }
}
