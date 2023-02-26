package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.simple_blocks.ISimpleBlock

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

        private fun blocksToDisplayString1(blocks: List<ISimpleBlock>): String = toDisplayString1(blocks.joinToString(separator = "") { it.toString() })
        fun blocksToDisplayString2(blocks: List<ISimpleBlock>): String = "[" + blocksToDisplayString1(blocks) + "]"

        private fun partsToDisplayString1(parts: List<IPart>): String = toDisplayString1(parts.joinToString(separator = "") { it.toString() })
        fun partsToDisplayString2(parts: List<IPart>): String = "[" + partsToDisplayString1(parts) + "]"

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

        fun shorten(s: String, maxLength: Int): String
        {
            if (s.length < maxLength)
                return s

            return s.substring(0, maxLength)
        }
    }
}
