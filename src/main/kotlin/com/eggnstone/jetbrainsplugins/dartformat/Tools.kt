package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.dotlin.C
import com.eggnstone.jetbrainsplugins.dartformat.dotlin.DotlinTools

class Tools
{
    companion object
    {
        fun isWhitespace(c: C): Boolean = DotlinTools.contains("\n\r\t ", c)

        fun isClosingBracket(c: C): Boolean = DotlinTools.contains("})]", c)
        fun isOpeningBracket(c: C): Boolean = DotlinTools.contains("{([", c)

        private fun charsToDisplayString1(chars: List<C>): String
        {
            var result = ""

            /* dotlin
            for (c in chars)
                result += toDisplayString1(c)
            */

            @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
            for (i in 0 until chars.size)
                result += toDisplayString1(chars[i])

            // dotlin
            //return toDisplayString1(chars.joinToString(separator = "") { "'$it'" })

            return result
        }

        fun charsToDisplayString2(chars: List<C>): String = "[" + charsToDisplayString1(chars) + "]"

        private fun toDisplayString1(s: String): String
        {
            return DotlinTools.replace(DotlinTools.replace(s, C("\r"), "\\r"), C("\n"), "\\n")
        }

        fun toDisplayString2(s: String): String = "\"" + toDisplayString1(s) + "\""

        private fun toDisplayString1(c: C): String = toDisplayString1(c.value)
        fun toDisplayString2(c: C): String = "'" + toDisplayString1(c) + "'"

        private fun stringsToDisplayString1(strings: List<String>): String
        {
            var result = ""

            /* dotlin
            for (s in strings)
                result += toDisplayString1(s)
            */

            @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
            for (i in 0 until strings.size)
                result += toDisplayString1(strings[i])

            // dotlin
            //return toDisplayString1(strings.joinToString(separator = "") { it })

            return result
        }

        //fun stringsToDisplayString2(strings: List<String>): String = "[" + stringsToDisplayString1(strings) + "]"

        fun getOpeningBracket(closingBracket: C): C
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
            when (closingBracket.value)
            {
                "}" -> return C("{")
                ")" -> return C("(")
                "]" -> return C("[")
                else -> throw DartFormatException("Unexpected closing bracket: $closingBracket")
            }
        }
    }
}
