package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

class Tools
{
    companion object
    {
        fun isWhitespace(c: String): Boolean = DotlinTools.contains("\n\r\t ", c)

        fun isClosingBracket(c: String): Boolean = DotlinTools.contains("})]", c)
        fun isOpeningBracket(c: String): Boolean = DotlinTools.contains("{([", c)

        private fun toDisplayString1(s: String): String
        {
            return DotlinTools.replace(DotlinTools.replace(s, "\r", "\\r"), "\n", "\\n")
        }

        fun toDisplayString2(s: String): String = "\"" + toDisplayString1(s) + "\""

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

        fun stringsToDisplayString2(strings: List<String>): String = "[" + stringsToDisplayString1(strings) + "]"

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
    }
}
