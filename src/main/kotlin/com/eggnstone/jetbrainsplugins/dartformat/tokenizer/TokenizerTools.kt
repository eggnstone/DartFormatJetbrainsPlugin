package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

class TokenizerTools
{
    companion object
    {
        fun isText(currentChar: Char): Boolean = "_".contains(currentChar)
                || currentChar in 'a'..'z'
                || currentChar in 'A'..'Z'
                || currentChar in '0'..'9'

        fun isWhiteSpace(currentChar: Char): Boolean = "\n\r\t ".contains(currentChar)

        fun toDisplayString(input: String): String = input.replace("\n", "\\n").replace("\r", "\\r")

        fun isSpecial(c: Char): Boolean = ":;,(){}[]".contains(c)
    }
}
