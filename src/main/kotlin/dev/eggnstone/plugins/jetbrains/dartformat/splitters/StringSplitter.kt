package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper

class StringSplitter
{
    companion object
    {
        fun split(s: String, delimiter: String, trim: Boolean): List<String>
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
                        if (trim)
                        {
                            val trimmedCurrentText = Tools.trimSimple(currentText)
                            if (StringWrapper.isNotEmpty(trimmedCurrentText))
                                result.add(trimmedCurrentText)
                        }
                        else
                            result.add(currentText)

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
                if (trim)
                {
                    val trimmedRest = Tools.trimSimple(rest)
                    if (StringWrapper.isEmpty(trimmedRest))
                    {
                        //TODO("DotlinTools.isEmpty(trimmedRest)")

                        /*if (rest.contains("\n") || rest.contains("\r"))
                            TODO("DotlinTools.isEmpty(trimmedRest)")*/

                        //TODO("Rest is empty s=${Tools.toDisplayStringShort(s.replace("*", "_"))} delimiter=${Tools.toDisplayStringShort(delimiter)} rest=${Tools.toDisplayStringShort(rest)} trim=$trim")
                        //result.add("/*rest is empty s=${Tools.toDisplayStringShort(s.replace("*", "_"))} delimiter=${Tools.toDisplayStringShort(delimiter)} rest=${Tools.toDisplayStringShort(rest)} trim=$trim*/")
                        //result.add("_EMPTY2_")
                    }
                    else
                        result.add(trimmedRest)
                }
                else
                    result.add(rest)
            }

            return result
        }
    }
}
