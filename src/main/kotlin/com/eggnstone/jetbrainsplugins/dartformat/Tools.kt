package com.eggnstone.jetbrainsplugins.dartformat

class Tools
{
    companion object
    {
        fun containsLineBreak(text: String): Boolean = text.contains("\n") || text.contains("\r")

        fun isLinkBreak(currentChar: Char): Boolean = "\n\r".contains(currentChar)

        fun isSpecial(c: Char): Boolean = ":;,(){}[]".contains(c)

        fun isWhiteSpace(currentChar: Char): Boolean = "\t ".contains(currentChar)

        fun toDisplayString(input: String): String = input.replace("\n", "\\n").replace("\r", "\\r")
    }
}
