package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.dotlin.DotlinChar

class Constants
{
    companion object
    {
        const val OPENING_CURLY_BRACKET = "{"
        const val CLOSING_CURLY_BRACKET = "}"

        const val OPENING_ROUND_BRACKET = "("
        const val CLOSING_ROUND_BRACKET = ")"

        const val OPENING_SQUARE_BRACKET = "["
        const val CLOSING_SQUARE_BRACKET = "]"

        const val ARROW = "=>"

        val COLON = DotlinChar(":")
        val COMMA = DotlinChar(",")
        val EQUAL = DotlinChar("=")
        val GREATER_THAN = DotlinChar(">")
        val PERIOD = DotlinChar(".")
        val SEMICOLON = DotlinChar(";")
    }
}
