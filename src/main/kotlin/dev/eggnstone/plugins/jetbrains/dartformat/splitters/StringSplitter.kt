package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

class StringSplitter
{
    companion object
    {
        fun split(s: String, delimiter: String, trim: Boolean): List<String>
        {
            /*if (s.contains("    abc"))
                TODO()*/

            DotlinLogger.log("StringSplitter.split: s=${Tools.toDisplayString(s)} delimiter=${Tools.toDisplayString(delimiter)} trim=$trim")

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
                        if (trim)
                        {
                            val trimmedCurrentText = Tools.trimSimple(currentText)
                            if (DotlinTools.isEmpty(trimmedCurrentText))
                                TODO("DotlinTools.isEmpty(trimmedCurrentText)")

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

            //DotlinLogger.log("    currentText: ${Tools.toDisplayString(currentText)}")
            //DotlinLogger.log("    loop rest:   ${Tools.toDisplayString(s.substring(i))}")

            val rest = currentText + DotlinTools.substring(s, i)
            //DotlinLogger.log("    rest:        ${Tools.toDisplayString(rest)}")

            if (DotlinTools.isNotEmpty(rest))
            {
                if (trim)
                {
                    val trimmedRest = Tools.trimSimple(rest)
                    if (DotlinTools.isEmpty(trimmedRest))
                    {
                        //TODO("DotlinTools.isEmpty(trimmedRest)")

                        /*if (rest.contains("\n") || rest.contains("\r"))
                            TODO("DotlinTools.isEmpty(trimmedRest)")*/

                        //TODO("Rest is empty s=${Tools.toDisplayString(s.replace("*", "_"))} delimiter=${Tools.toDisplayString(delimiter)} rest=${Tools.toDisplayString(rest)} trim=$trim")
                        //result.add("/*rest is empty s=${Tools.toDisplayString(s.replace("*", "_"))} delimiter=${Tools.toDisplayString(delimiter)} rest=${Tools.toDisplayString(rest)} trim=$trim*/")
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
