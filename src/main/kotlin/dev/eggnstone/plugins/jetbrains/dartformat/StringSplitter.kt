package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

class StringSplitter
{
    companion object
    {
        fun split(s: String, delimiter: String, trim: Boolean = false): List<String>
        {
            @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
            if (delimiter.length == 0)
                throw DartFormatException("delimiter.length == 0")

            @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
            if (s.length == 0)
                return listOf("")

            val result = mutableListOf<String>()

            var currentText = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            var i = 0
            while (i < s.length - delimiter.length + 1)
            {
                @Suppress("ReplaceGetOrSet")
                val candidate = DotlinTools.substring(s, i, i + delimiter.length)

                if (candidate == delimiter)
                {
                    if (DotlinTools.isNotEmpty(currentText))
                    {
                        if (DotlinTools.isEmpty(currentText.trim()))
                            TODO("DotlinTools.isEmpty(currentText.trim())")

                        result.add(currentText.trim())
                        currentText = ""
                    }

                    result.add("") // indicator for found delimiter
                    i += delimiter.length
                    continue
                }

                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()
                currentText += c
                i++
            }

            //DotlinLogger.log("    currentText: ${Tools.toDisplayString(currentText)}")
            //DotlinLogger.log("    loop rest:   ${Tools.toDisplayString(s.substring(i))}")

            val rest = currentText + DotlinTools.substring(s, i)
            //DotlinLogger.log("    rest:        ${Tools.toDisplayString(rest)}")

            if (DotlinTools.isNotEmpty(rest))
            {
                if (DotlinTools.isEmpty(rest.trim()))
                    TODO("DotlinTools.isEmpty(rest.trim())")

                result.add(rest.trim())
            }

            return result
        }
    }
}
