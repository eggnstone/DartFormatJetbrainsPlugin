package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.blocks.IBlock
import com.eggnstone.jetbrainsplugins.dartformat.indenter.IIndent
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import java.util.*

class ToolsOld
{
    companion object
    {
        val classKeywords = arrayOf(
            "abstract class",
            "class",
            "extends",
            "implements",
            "with"
        )

        val keywords = arrayOf(
            //"case", // ?
            "catch",
            "do",
            "else",
            "finally",
            "for",
            "if",
            "switch",
            "try",
            "while"
        )

        fun containsLineBreak(text: String): Boolean = text.contains("\n") || text.contains("\r")

        fun isKeyword(s: String): Boolean = keywords.contains(s)

        // TODO: add = and so on
        fun isSpecial(c: Char): Boolean = ".:;,(){}[]<>".contains(c)

        fun isWhitespace(c: Char): Boolean = "\n\r\t ".contains(c)

        fun isWhiteSpaceOld(c: Char): Boolean = "\t ".contains(c)

        fun isClosingBracket(c: Char): Boolean = "})]".contains(c)
        fun isOpeningBracket(c: Char): Boolean = "{([".contains(c)

        fun shorten(s: String, maxLength: Int): String
        {
            if (s.length < maxLength)
                return s

            return DotlinTools.substring(s,0, maxLength)
        }

        private fun blocksToDisplayString1(blocks: List<IBlock>): String = toDisplayString1(blocks.joinToString(separator = "") { it.toString() })
        fun blocksToDisplayString2(blocks: List<IBlock>): String = "[" + blocksToDisplayString1(blocks) + "]"

        private fun charsToDisplayString1(chars: List<Char>): String = toDisplayString1(chars.joinToString(separator = "") { "'$it'" })
        fun charsToDisplayString2(chars: List<Char>): String = "[" + charsToDisplayString1(chars) + "]"

        private fun toDisplayString1(s: String): String = s.replace("\n", "\\n").replace("\r", "\\r")
        fun toDisplayString2(s: String): String = "\"" + toDisplayString1(s) + "\""

        private fun toDisplayString1(c: Char): String = toDisplayString1(c.toString())
        fun toDisplayString2(c: Char): String = "'" + toDisplayString1(c) + "'"

        private fun indentsToDisplayString1(indents: Stack<IIndent>): String = toDisplayString1(indents.joinToString(separator = ",") { it.toString() })
        fun indentsToDisplayString2(indents: Stack<IIndent>): String = "[" + indentsToDisplayString1(indents) + "]"

        private fun stringsToDisplayString1(strings: List<String>): String = toDisplayString1(strings.joinToString(separator = "") { it })
        fun stringsToDisplayString2(strings: List<String>): String = "[" + stringsToDisplayString1(strings) + "]"

        private fun tokensToDisplayString1(tokens: List<IToken>): String = toDisplayString1(tokens.joinToString { "\"" + it.recreate() + "\"" })
        fun tokensToDisplayString2(tokens: List<IToken>): String = "[" + tokensToDisplayString1(tokens) + "]"

        fun getClosingBracket(openingBracket: Char): Char
        {
            return when (openingBracket)
            {
                '{' -> '}'
                '(' -> ')'
                '[' -> ']'
                else -> throw DartFormatException("Unexpected opening bracket: $openingBracket")
            }
        }

        fun getOpeningBracket(closingBracket: Char): Char
        {
            return when (closingBracket)
            {
                '}' -> '{'
                ')' -> '('
                ']' -> '['
                else -> throw DartFormatException("Unexpected closing bracket: $closingBracket")
            }
        }
    }
}
