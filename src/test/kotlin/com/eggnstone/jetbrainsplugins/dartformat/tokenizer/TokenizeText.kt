package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken2
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeText
{
    @Test
    fun text()
    {
        val inputText = "sometextwithoutwhitespace"
        val expectedTokens = arrayListOf(TextToken2(inputText))

        val tokenizer = Tokenizer()
        val textTokenizer = TextTokenizer()
        val actualTokens = textTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }
}
