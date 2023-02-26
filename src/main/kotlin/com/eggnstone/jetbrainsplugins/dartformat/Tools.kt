package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.dotlin.DotlinChar
import com.eggnstone.jetbrainsplugins.dartformat.dotlin.DotlinTools

class Tools
{
    companion object
    {
        fun isWhitespace(c: DotlinChar): Boolean = DotlinTools.contains("\n\r\t ", c)

        fun isClosingBracket(c: DotlinChar): Boolean = DotlinTools.contains("})]", c)
        fun isOpeningBracket(c: DotlinChar): Boolean = DotlinTools.contains("{([", c)

        private fun charsToDisplayString1(chars: List<DotlinChar>): String
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

        fun charsToDisplayString2(chars: List<DotlinChar>): String = "[" + charsToDisplayString1(chars) + "]"

        private fun toDisplayString1(s: String): String
        {
            return DotlinTools.replace(DotlinTools.replace(s, DotlinChar("\r"), "\\r"), DotlinChar("\n"), "\\n")
        }

        fun toDisplayString2(s: String): String = "\"" + toDisplayString1(s) + "\""

        private fun toDisplayString1(c: DotlinChar): String = toDisplayString1(c.value)
        fun toDisplayString2(c: DotlinChar): String = "'" + toDisplayString1(c) + "'"

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

        fun getOpeningBracket(closingBracket: DotlinChar): DotlinChar
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
                "}" -> return DotlinChar("{")
                ")" -> return DotlinChar("(")
                "]" -> return DotlinChar("[")
                else -> throw DartFormatException("Unexpected closing bracket: $closingBracket")
            }
        }
    }
}
