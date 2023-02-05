package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken2

class TextTokenizer
{
    fun tokenize(input: String): ArrayList<IToken> = arrayListOf(TextToken2(input))
}
