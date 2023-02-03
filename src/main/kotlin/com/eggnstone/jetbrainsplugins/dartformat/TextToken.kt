package com.eggnstone.jetbrainsplugins.dartformat

class TextToken(val delimiter: String) : IToken
{
    override fun toString(): String
    {
        return delimiter
    }
}
