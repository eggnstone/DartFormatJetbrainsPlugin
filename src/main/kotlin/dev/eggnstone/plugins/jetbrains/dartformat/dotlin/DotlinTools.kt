package dev.eggnstone.plugins.jetbrains.dartformat.dotlin

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

class DotlinTools
{
    // .add() should be replaced with +=

    companion object
    {
        /*// String.contains
        fun containsCharOld(s: String, searchChar: String): Boolean
        {
            //if (DotlinLogger.isEnabled) DotlinLogger.log("DotlinTools.containsChar(${Tools.toDisplayString(s)}, ${Tools.toDisplayString(searchChar)})")

            if (searchChar.length != 1)
                TODO("untested") // throw DartFormatException("Use containsString() instead!")

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
        fun containsStringOls(s: String, searchText: String): Boolean
        {
            //if (DotlinLogger.isEnabled) DotlinLogger.log("DotlinTools.containsString(${Tools.toDisplayString(s)}, ${Tools.toDisplayString(searchText)})")

            @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
            if (searchText.length == 0)
                return true

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length - searchText.length + 1)
            {
                //if (DotlinLogger.isEnabled) DotlinLogger.log("  in: ${Tools.toDisplayString2(substring(s, i, searchText.length))}")
                if (StringWrapper.substring(s, i, i + searchText.length) == searchText)
                    return true
            }

            return false
        }*/

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

        // List<T>.isEmpty
        @Suppress("ReplaceSizeCheckWithIsNotEmpty")
        fun <T> isNotEmpty(l: List<T>): Boolean = l.size > 0

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

        // String.startsWith
        fun startsWith(s: String, searchText: String): Boolean
        {
            if (s.length < searchText.length)
                return false

            return StringWrapper.substring(s, 0, searchText.length) == searchText
        }

        // String.endsWith
        fun endsWith(s: String, searchText: String): Boolean
        {
            if (s.length < searchText.length)
                TODO("untested") // return false

            return StringWrapper.substring(s, s.length - searchText.length) == searchText
        }

        /*// String.substring
        fun substring(s: String, startIndex: Int, endIndex: Int = -1): String
        {
            return s.substring(startIndex,endIndex)
            if (startIndex < 0)
                throw DartFormatException("startIndex < 0")

            if (endIndex > s.length)
                throw DartFormatException("endIndex > s.length")

            var result = ""

            val maxIndex = if (endIndex == -1) s.length else minOf(s.length, endIndex)
            //if (DotlinLogger.isEnabled) DotlinLogger.log("s:            ${Tools.toDisplayString2(s)}")
            //if (DotlinLogger.isEnabled) DotlinLogger.log("  startIndex: $startIndex")
            //if (DotlinLogger.isEnabled) DotlinLogger.log("  endIndex:   $endIndex")
            //if (DotlinLogger.isEnabled) DotlinLogger.log("  s.length:   ${s.length}")
            //if (DotlinLogger.isEnabled) DotlinLogger.log("  maxIndex:   $maxIndex")

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in startIndex until maxIndex)
            {
                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()
                result += c
            }

            //if (DotlinLogger.isEnabled) DotlinLogger.log("  result:     ${Tools.toDisplayString2(result)}")
            return result
        }*/

        // String.trim
        fun trim(s: String): String
        {
            return trimStart(trimEnd(s))
        }

        fun trimStart(s: String): String
        {
            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()
                if (!Tools.isWhitespace(c))
                    return StringWrapper.substring(s, i)
            }

            return ""
        }

        fun trimEnd(s: String): String
        {
            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in s.length - 1 downTo 0)
            {
                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()
                if (!Tools.isWhitespace(c))
                    return StringWrapper.substring(s, 0, i + 1)
            }


            return ""
        }

        /*fun toMutableListOfString(s: String): MutableList<String>
        {
            val result = mutableListOf<String>()
            //val result :MutableList<String> = mutableListOf<String>()

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val c = s.get(i).toString()
                //result += c dotlin
                result.add(c)
            }

            return result
        }*/

        fun last(list: List<String>): String
        {
            return list[list.size - 1]
        }

        // String.indexOf
        fun indexOf(s: String, searchText: String): Int
        {
            //if (DotlinLogger.isEnabled) DotlinLogger.log("DotlinTools.indexOf(${Tools.toDisplayString(s)}, ${Tools.toDisplayString(searchText)})")

            @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
            if (searchText.length == 0)
                TODO("untested") // return 0

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length - searchText.length + 1)
            {
                //if (DotlinLogger.isEnabled) DotlinLogger.log("  in: ${Tools.toDisplayString2(substring(s, i, searchText.length))}")
                if (StringWrapper.substring(s, i, i + searchText.length) == searchText)
                    return i
            }

            return -1
        }

        fun <T> clone(inputList: List<T>): MutableList<T>
        {
            val outputList = mutableListOf<T>()
            outputList.addAll(inputList)
            return outputList
        }
    }
}
