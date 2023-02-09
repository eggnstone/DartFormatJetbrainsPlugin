package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.Constants
import com.eggnstone.jetbrainsplugins.dartformat.Tools

class SpecialToken(val text: String) : IToken
{
    companion object
    {
        val OPENING_ANGLE_BRACKET = SpecialToken(Constants.OPENING_ANGLE_BRACKET)
        val CLOSING_ANGLE_BRACKET = SpecialToken(Constants.CLOSING_ANGLE_BRACKET)

        val OPENING_POINTY_BRACKET = SpecialToken(Constants.OPENING_POINTY_BRACKET)
        val CLOSING_POINTY_BRACKET = SpecialToken(Constants.CLOSING_POINTY_BRACKET.toString())

        val OPENING_ROUND_BRACKET = SpecialToken(Constants.OPENING_ROUND_BRACKET)
        val CLOSING_ROUND_BRACKET = SpecialToken(Constants.CLOSING_ROUND_BRACKET)

        val OPENING_SQUARE_BRACKET = SpecialToken(Constants.OPENING_SQUARE_BRACKET)
        val CLOSING_SQUARE_BRACKET = SpecialToken(Constants.CLOSING_SQUARE_BRACKET)

        val ARROW = SpecialToken(Constants.ARROW)
        val COLON = SpecialToken(Constants.COLON.toString())
        val COMMA = SpecialToken(Constants.COMMA.toString())
        val EQUAL = SpecialToken(Constants.EQUAL.toString())
        val PERIOD = SpecialToken(Constants.PERIOD.toString())
        val SEMICOLON = SpecialToken(Constants.SEMICOLON.toString())
    }

    val isClosingBracket get() = this == CLOSING_ANGLE_BRACKET || this == CLOSING_POINTY_BRACKET || this == CLOSING_ROUND_BRACKET || this == CLOSING_SQUARE_BRACKET

    val isOpeningBracket get() = this == OPENING_ANGLE_BRACKET || this == OPENING_POINTY_BRACKET || this == OPENING_ROUND_BRACKET || this == OPENING_SQUARE_BRACKET

    override fun equals(other: Any?): Boolean = other is SpecialToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "Special(${Tools.toDisplayString(text)})"
}
