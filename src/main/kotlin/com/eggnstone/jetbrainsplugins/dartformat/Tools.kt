package com.eggnstone.jetbrainsplugins.dartformat

class Tools
{
    companion object
    {
        fun containsLineBreak(text: String): Boolean = text.contains("\n") || text.contains("\r")

        fun isKeyword(s: String): Boolean = arrayOf(
            "do",
            "for",
            "if",
            "while"
        ).contains(s)

        fun isSpecial(c: Char): Boolean = ".:;,(){}[]<>".contains(c)

        //fun isSpecial(c: Char, previousChar: Char?): Boolean = previousChar == '=' && c == '>'

        fun isWhiteSpace(currentChar: Char): Boolean = "\t ".contains(currentChar)

        fun toDisplayString(input: String): String = input.replace("\n", "\\n").replace("\r", "\\r")
    }
}
