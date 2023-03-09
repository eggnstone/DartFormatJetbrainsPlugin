package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class Tools
{
    companion object
    {
        private const val closingBrackets = "])}"
        private const val openingBrackets = "{[("

        fun getOpeningBracket(closingBracket: String): String
        {
            /* dotlin
            return when (closingBracket.value)
            {
                "}" -> C("{")
                ")" -> C("(")
                "]" -> C("[")
                else -> throw DartFormatException("Unexpected closing bracket: $closingBracket")
            }
            */
            @Suppress("LiftReturnOrAssignment") // dotlin
            when (closingBracket)
            {
                "}" -> return "{"
                ")" -> return "("
                "]" -> return "["
                else -> throw DartFormatException("Unexpected closing bracket: $closingBracket")
            }
        }

        fun getClosingBracket(closingBracket: String): String
        {
            @Suppress("LiftReturnOrAssignment") // dotlin
            when (closingBracket)
            {
                "{" -> return "}"
                "(" -> return ")"
                "[" -> return "]"
                else -> throw DartFormatException("Unexpected closing bracket: $closingBracket")
            }
        }

        fun isClosingBracket(c: String): Boolean = DotlinTools.containsChar(closingBrackets, c)
        fun isOpeningBracket(c: String): Boolean = DotlinTools.containsChar(openingBrackets, c)
        fun isBracket(c: String): Boolean = DotlinTools.containsChar(openingBrackets + closingBrackets, c)
        fun isWhitespace(c: String): Boolean = DotlinTools.containsChar("\n\r\t ", c)

        fun shorten(s: String, maxLength: Int): String
        {
            if (s.length < maxLength)
                return s

            return DotlinTools.substring(s, 0, maxLength) // dotlin
            //return s.substring(0, maxLength)
        }

        fun toDisplayString(s: String): String = "\"" + toDisplayStringSimple(s) + "\""
        fun toDisplayStringSimple(s: String): String = DotlinTools.replace(DotlinTools.replace(s, "\r", "\\r"), "\n", "\\n")

        fun toDisplayStringForParts(parts: List<IPart>): String = "[" + toDisplayStringForPartsInternal(parts) + "]"
        fun toDisplayStringForStrings(strings: List<String>): String = "[" + toDisplayStringForStringsInternal(strings) + "]"

        private fun toDisplayStringForPartsInternal(parts: List<IPart>): String
        {
            var result = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
            for (i in 0 until parts.size) // workaround for dotlin
            {
                @Suppress("ReplaceGetOrSet") // workaround for dotlin
                val part = parts.get(i) // workaround for dotlin
                if (DotlinTools.isNotEmpty(result))
                    result += ","

                result += toDisplayStringSimple(part.toString())
            }

            return result
        }

        private fun toDisplayStringForStringsInternal(strings: List<String>): String
        {
            var result = ""

            /* dotlin
            for (s in strings)
                result += toDisplayString1(s)
            */

            @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
            for (i in 0 until strings.size)
            {
                if (i > 0)
                    result += ","

                result += toDisplayString(strings[i])
            }

            // dotlin
            //return toDisplayString1(strings.joinToString(separator = "") { it })

            return result
        }

        fun trimSimple(s: String): String
        {
            //return s
            //return trimEndSimple(s)
            return trimStartSimple(trimEndSimple(s))
            /*val t = trimStartSimple(trimEndSimple(s))
            if (t == s)
                return s

            DotlinLogger.log("s: $s")
            DotlinLogger.log("t: $t")
            TODO()
            return "1${s}2"*/
        }

        @Suppress("MemberVisibilityCanBePrivate")
        fun trimStartSimple(s: String): String
        {
            var startText = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
                val c = s.get(i).toString() // workaround for dotlin for: for (c in text)
                if (c == "\n" || c == "\r")
                {
                    startText += c
                    continue
                }

                if (c == "\t" || c == " ")
                    continue

                return startText + DotlinTools.substring(s, i)
            }

            return startText
        }

        @Suppress("MemberVisibilityCanBePrivate")
        fun trimEndSimple(s: String): String
        {
            var endText = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
            for (i in s.length - 1 downTo 0)
            {
                @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
                val c = s.get(i).toString() // workaround for dotlin for: for (c in text)
                if (c == "\n" || c == "\r")
                {
                    endText = c + endText
                    continue
                }

                if (c == "\t" || c == " ")
                    continue

                return DotlinTools.substring(s, 0, i + 1) + endText
            }

            return endText
        }
    }
}
