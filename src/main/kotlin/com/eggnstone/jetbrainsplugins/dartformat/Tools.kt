package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.blocks.IBlock
import com.eggnstone.jetbrainsplugins.dartformat.indenter.IIndent
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import java.util.*

class Tools
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
        fun isSpecial(input: Char): Boolean = ".:;,(){}[]<>".contains(input)

        fun isWhitespace(input: Char): Boolean = "\n\r\t ".contains(input)

        fun isWhiteSpaceOld(input: Char): Boolean = "\t ".contains(input)

        fun shorten(s: String, maxLength: Int): String
        {
            if (s.length < maxLength)
                return s

            return s.substring(0, maxLength)
        }

        fun toDisplayString(input: String): String = input.replace("\n", "\\n").replace("\r", "\\r")

        fun toBlocksDisplayString(input: List<IBlock>): String = input.joinToString(separator = "") { toDisplayString(it.toString()) }

        fun toDisplayString(input: List<String>): String = input.joinToString(separator = "") { toDisplayString(it) }

        fun toIndentsDisplayString(input: Stack<IIndent>): String = input.joinToString(separator = ",") { it.toString() }

        fun toTokensDisplayString(input: List<IToken>): String = toDisplayString(input.joinToString { "\"" + it.recreate() + "\"" })
    }
}
