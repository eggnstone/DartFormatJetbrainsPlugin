package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.TokenizerTools

class SpecialToken(val text: String) : IToken
{
    companion object
    {
        val OPENING_ANGLE_BRACKET = SpecialToken("{")
        val OPENING_ROUND_BRACKET = SpecialToken("(")
        val OPENING_SQUARE_BRACKET = SpecialToken("[")
        val CLOSING_ANGLE_BRACKET = SpecialToken("}")
        val CLOSING_ROUND_BRACKET = SpecialToken(")")
        val CLOSING_SQUARE_BRACKET = SpecialToken("]")
        val COLON = SpecialToken(":")
        val COMMA = SpecialToken(",")
        val SEMICOLON = SpecialToken(";")
    }

    val isClosingBracket get() = this == CLOSING_ANGLE_BRACKET || this == CLOSING_ROUND_BRACKET || this == CLOSING_SQUARE_BRACKET

    override fun equals(other: Any?): Boolean = other is SpecialToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "Special(${TokenizerTools.toDisplayString(text)})"
}
