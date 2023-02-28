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
            //DotlinLogger.log("DotlinTools.containsChar(${Tools.toDisplayString(s)}, ${Tools.toDisplayString(searchChar)})")

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
            //DotlinLogger.log("DotlinTools.containsString(${Tools.toDisplayString(s)}, ${Tools.toDisplayString(searchText)})")

            @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
            if (searchText.length == 0)
                return true

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length - searchText.length + 1)
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

        // String.isBlank
        fun isBlank(s: String): Boolean = isEmpty(trim(s))

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
        // Warning! This is my own implementation and actually differs from Kotlin's String.split!
        fun split(s: String, delimiter: String): List<String>
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
                val candidate = substring(s, i, i + delimiter.length)

                if (candidate == delimiter)
                {
                    if (isNotEmpty(currentText))
                    {
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

            val rest = currentText + substring(s, i)
            //DotlinLogger.log("    rest:        ${Tools.toDisplayString(rest)}")

            if (isNotEmpty(rest))
                result.add(rest)

            return result
        }

        // String.startsWith
        fun startsWith(s: String, searchText: String): Boolean
        {
            if (s.length < searchText.length)
                return false

            return substring(s, 0, searchText.length) == searchText
        }

        // String.substring
        fun substring(s: String, startIndex: Int, endIndex: Int = -1): String
        {
            if (startIndex < 0)
                throw DartFormatException("startIndex < 0")

            if (endIndex > s.length)
                throw DartFormatException("endIndex > s.length")

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
