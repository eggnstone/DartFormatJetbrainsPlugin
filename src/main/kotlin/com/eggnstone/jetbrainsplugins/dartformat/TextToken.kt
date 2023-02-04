package com.eggnstone.jetbrainsplugins.dartformat

class TextToken(val text: String) : IToken
{
    override fun toString(): String
    {
        return text
    }
}
