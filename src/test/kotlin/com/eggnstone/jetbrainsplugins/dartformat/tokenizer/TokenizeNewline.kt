package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.DelimiterToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeNewline
{
    @Test
    fun newlineN()
    {
        val input = "a\nb"
        val expectedTokens = arrayListOf(TextToken("a"), DelimiterToken("\n"), TextToken("b"))

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(actualTokens, equalTo(expectedTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertThat(actualText, equalTo(input))
    }

    @Test
    fun newlineR()
    {
        val input = "a\rb"
        val expectedTokens = arrayListOf(TextToken("a"), DelimiterToken("\r"), TextToken("b"))

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(actualTokens, equalTo(expectedTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertThat(actualText, equalTo(input))
    }

    @Test
    fun newlineRN()
    {
        val input = "a\r\nb"
        val expectedTokens = arrayListOf(TextToken("a"), DelimiterToken("\r\n"), TextToken("b"))

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(actualTokens, equalTo(expectedTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertThat(actualText, equalTo(input))
    }

    @Test
    fun newlineNR()
    {
        val input = "a\n\rb"
        val expectedTokens = arrayListOf(TextToken("a"), DelimiterToken("\n\r"), TextToken("b"))

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(actualTokens, equalTo(expectedTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertThat(actualText, equalTo(input))
    }
}
