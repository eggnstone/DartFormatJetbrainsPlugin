package com.eggnstone.jetbrainsplugins.dartformat.tokens

class WhiteSpaceToken(val delimiter: String) : IToken
{
    //val isNewline: Boolean = delimiter == "\n" || delimiter == "\n\r" || delimiter == "\r" || delimiter == "\r\n"

    override fun equals(other: Any?): Boolean = other is WhiteSpaceToken && delimiter == other.delimiter

    override fun hashCode(): Int = delimiter.hashCode()

    override fun recreate(): String = delimiter

    override fun toString(): String = delimiter.replace("\n", "\\n").replace("\r", "\\r")
}
