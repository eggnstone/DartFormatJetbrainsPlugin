package com.eggnstone.jetbrainsplugins.dartformat.tokens

import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.TokenizerTools

class SpecialToken(val text: String) : IToken
{
    companion object
    {
        val OPENING_BRACKET = SpecialToken("(")
        val OPENING_ANGLE_BRACKET = SpecialToken("{")
        val OPENING_SQUARE_BRACKET = SpecialToken("[")
        val CLOSING_BRACKET = SpecialToken(")")
        val CLOSING_ANGLE_BRACKET = SpecialToken("}")
        val CLOSING_SQUARE_BRACKET = SpecialToken("]")
        val COLON = SpecialToken(":")
        val COMMA = SpecialToken(",")
        val SEMICOLON = SpecialToken(";")
    }

    override fun equals(other: Any?): Boolean = other is SpecialToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "Special(${TokenizerTools.toDisplayString(text)})"
}
