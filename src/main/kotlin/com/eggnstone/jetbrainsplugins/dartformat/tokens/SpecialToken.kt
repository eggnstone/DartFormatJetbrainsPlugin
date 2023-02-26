package com.eggnstone.jetbrainsplugins.dartformat.tokens

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld

class SpecialToken(val text: String) : IToken
{
    companion object
    {
        val OPENING_CURLY_BRACKET = SpecialToken(Constants.OPENING_CURLY_BRACKET)
        val CLOSING_CURLY_BRACKET = SpecialToken(Constants.CLOSING_CURLY_BRACKET)

        val OPENING_ROUND_BRACKET = SpecialToken(Constants.OPENING_ROUND_BRACKET)
        val CLOSING_ROUND_BRACKET = SpecialToken(Constants.CLOSING_ROUND_BRACKET)

        val OPENING_SQUARE_BRACKET = SpecialToken(Constants.OPENING_SQUARE_BRACKET)
        val CLOSING_SQUARE_BRACKET = SpecialToken(Constants.CLOSING_SQUARE_BRACKET)

        val ARROW = SpecialToken(Constants.ARROW)
        val COMMA = SpecialToken(Constants.COMMA)
    }

    val isClosingBracket get() = this == CLOSING_CURLY_BRACKET || this == CLOSING_ROUND_BRACKET || this == CLOSING_SQUARE_BRACKET

    val isOpeningBracket get() = this == OPENING_CURLY_BRACKET || this == OPENING_ROUND_BRACKET || this == OPENING_SQUARE_BRACKET

    override fun equals(other: Any?): Boolean = other is SpecialToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "Special(${ToolsOld.toDisplayString2(text)})"
}
