package dev.eggnstone.plugins.jetbrains.dartformat.dotlin

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools

class DotlinTools
{
    // .add() should be replaced with +=

    companion object
    {
        // String.contains
        fun containsChar(s: String, searchChar: String): Boolean
        {
            if (searchChar.length != 1)
                throw DartFormatException("Use containsString() instead!")

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()
                if (c == searchChar)
                    return true
            }

            return false
        }

        // String.contains
        fun containsString(s: String, searchText: String): Boolean
        {
            //DotlinLogger.log("Searching: ${Tools.toDisplayString2(searchText)}")

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length - searchText.length)
            {
                //DotlinLogger.log("  in: ${Tools.toDisplayString2(substring(s, i, searchText.length))}")
                if (substring(s, i, i + searchText.length) == searchText)
                    return true
            }

            return false
        }

        // String.repeat
        fun getSpaces(count: Int): String
        {
            //  " ".repeat
            var result = ""

            for (i in 0 until count)
                result += " "

            return result
        }

        // String.isEmpty
        @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
        fun isEmpty(s: String) = s.length == 0

        // List<T>.isEmpty
        @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
        fun <T> isEmpty(l: List<T>): Boolean = l.size == 0

        // String.isNotEmpty
        @Suppress("ReplaceSizeCheckWithIsNotEmpty")
        fun isNotEmpty(s: String) = s.length > 0

        // kotlin minOf
        @Suppress("MemberVisibilityCanBePrivate")
        fun minOf(a: Int, b: Int): Int = if (a < b) a else b

        // String.replace
        fun replace(s: String, searchChar: String, replaceText: String): String
        {
            var result = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()
                result += if (c == searchChar) replaceText else c
            }

            return result
        }

        // String.split
        fun split(s: String, delimiter: String): List<String>
        {
            val result = mutableListOf<String>()

            var currentText = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()

                if (c == delimiter)
                {
                    if (isNotEmpty(currentText))
                    {
                        result.add(currentText)
                        currentText = ""
                    }

                    continue
                }

                currentText += c
            }

            if (isNotEmpty(currentText))
                result.add(currentText)

            return result
        }

        // String.startsWith
        fun startsWith(s: String, searchText: String): Boolean
        {
            if (s.length < searchText.length)
                return false

            return substring(s, searchText.length) == searchText
        }

        // String.substring
        fun substring(s: String, startIndex: Int, endIndex: Int = -1): String
        {
            var result = ""

            val maxIndex = if (endIndex == -1) s.length else minOf(s.length, endIndex)
            //DotlinLogger.log("s:            ${Tools.toDisplayString2(s)}")
            //DotlinLogger.log("  startIndex: $startIndex")
            //DotlinLogger.log("  endIndex:   $endIndex")
            //DotlinLogger.log("  s.length:   ${s.length}")
            //DotlinLogger.log("  maxIndex:   $maxIndex")

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in startIndex until maxIndex)
            {
                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()
                result += c
            }

            //DotlinLogger.log("  result:     ${Tools.toDisplayString2(result)}")
            return result
        }

        // String.trim
        fun trim(s: String): String
        {
            var startIndex = 0
            var endIndex = s.length - 1

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()
                if (!Tools.isWhitespace(c))
                    break

                startIndex++
            }

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in s.length - 1 downTo startIndex + 1)
            {
                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()
                if (!Tools.isWhitespace(c))
                    break

                endIndex--
            }

            return substring(s, startIndex, endIndex + 1)
        }
    }
}
