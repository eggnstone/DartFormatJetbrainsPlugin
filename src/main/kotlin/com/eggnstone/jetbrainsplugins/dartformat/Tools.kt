package com.eggnstone.jetbrainsplugins.dartformat

class Tools
{
    companion object
    {
        val OPENING_ANGLE_BRACKET = "{"
        val CLOSING_ANGLE_BRACKET = "}"

        val OPENING_POINTY_BRACKET = "<"
        val CLOSING_POINTY_BRACKET_CHAR = '>'
        val CLOSING_POINTY_BRACKET = CLOSING_POINTY_BRACKET_CHAR.toString()

        val OPENING_ROUND_BRACKET = "("
        val CLOSING_ROUND_BRACKET = ")"

        val OPENING_SQUARE_BRACKET = "["
        val CLOSING_SQUARE_BRACKET = "]"

        val ARROW = "=>"
        val COLON_CHAR = ':'
        val COLON = COLON_CHAR.toString()
        val COMMA_CHAR = ','
        val COMMA = COMMA_CHAR.toString()
        val EQUAL_CHAR = '='
        val EQUAL = EQUAL_CHAR.toString()
        val PERIOD_CHAR = '.'
        val PERIOD = PERIOD_CHAR.toString()
        val SEMICOLON_CHAR = ';'
        val SEMICOLON = SEMICOLON_CHAR.toString()

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
