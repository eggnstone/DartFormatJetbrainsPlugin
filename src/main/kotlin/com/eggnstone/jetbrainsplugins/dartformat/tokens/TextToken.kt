package com.eggnstone.jetbrainsplugins.dartformat.tokens

class TextToken(val text: String) : IToken
{
    override fun toString(): String
    {
        return text
    }
}
