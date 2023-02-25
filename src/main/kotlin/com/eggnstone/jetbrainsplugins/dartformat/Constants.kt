package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.dotlin.C

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

        val COLON = C(":")
        val COMMA = C(",")
        val EQUAL = C("=")
        val GREATER_THAN = C(">")
        val PERIOD = C(".")
        val SEMICOLON = C(";")
    }
}
