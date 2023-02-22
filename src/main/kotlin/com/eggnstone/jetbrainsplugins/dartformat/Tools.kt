package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.indenter.IIndent
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import java.util.*

class Tools
{
    companion object
    {
        val keywords = arrayOf(
            "case", // ?
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

        fun isWhiteSpace(input: Char): Boolean = "\t ".contains(input)

        fun toDisplayString(input: String): String = input.replace("\n", "\\n").replace("\r", "\\r")

        fun toDisplayString(input: List<String>): String = input.joinToString(separator = "") { toDisplayString(it) }

        fun toString(input: List<IToken>): String = toDisplayString(input.joinToString { "\"" + it.recreate() + "\"" })

        fun toString(input: Stack<IIndent>): String = input.joinToString(separator = ",") { it.toString() }
    }
}
