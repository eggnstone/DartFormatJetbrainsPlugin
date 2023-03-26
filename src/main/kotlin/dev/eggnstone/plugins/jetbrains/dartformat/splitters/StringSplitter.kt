package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

class StringSplitter
{
    companion object
    {
        fun split(s: String, delimiter: String, trim: Boolean): List<String> = split(s, delimiter, trim, trim)

        fun split(s: String, delimiter: String, trimStart: Boolean, trimEnd: Boolean): List<String>
        {
            //if (DotlinLogger.isEnabled) DotlinLogger.log("StringSplitter.split: s=${Tools.toDisplayStringShort(s)} delimiter=${Tools.toDisplayStringShort(delimiter)} trim=$trim")

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
                val candidate = StringWrapper.substring(s, i, i + delimiter.length)

                if (candidate == delimiter)
                {
                    if (StringWrapper.isNotEmpty(currentText))
                    {
                        val trimmedCurrentText: String = if (trimStart && trimEnd)
                            Tools.trimSimple(currentText)
                        else if (trimStart)
                            Tools.trimStartSimple(currentText)
                        else if (trimEnd)
                            Tools.trimEndSimple(currentText)
                        else
                            currentText

                        if (StringWrapper.isNotEmpty(trimmedCurrentText))
                            result.add(trimmedCurrentText)

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

            //if (DotlinLogger.isEnabled) DotlinLogger.log("    currentText: ${Tools.toDisplayStringShort(currentText)}")
            //if (DotlinLogger.isEnabled) DotlinLogger.log("    loop rest:   ${Tools.toDisplayStringShort(s.substring(i))}")

            val rest = currentText + StringWrapper.substring(s, i)
            //if (DotlinLogger.isEnabled) DotlinLogger.log("    rest:        ${Tools.toDisplayStringShort(rest)}")

            if (StringWrapper.isNotEmpty(rest))
            {
                val trimmedRest: String = if (trimStart && trimEnd)
                    Tools.trimSimple(rest)
                else if (trimStart)
                    Tools.trimStartSimple(rest)
                else if (trimEnd)
                    Tools.trimEndSimple(rest)
                else
                    rest

                if (StringWrapper.isNotEmpty(trimmedRest))
                    result.add(trimmedRest)
            }

            return result
        }
    }
}
