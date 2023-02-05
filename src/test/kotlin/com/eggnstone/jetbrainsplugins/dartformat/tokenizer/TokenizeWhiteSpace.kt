package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeWhiteSpace
{
    @Test
    fun spaceAtTextStart()
    {
        val inputText = " b"
        val expectedTokens = arrayListOf(
            WhiteSpaceToken(" "),
            UnknownToken("b")
        )

        val tokenizer = Tokenizer()
        val whiteSpaceTokenizer = WhiteSpaceTokenizer()
        val actualTokens = whiteSpaceTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun spaceAtTextMiddle()
    {
        val inputText = "a b"
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            WhiteSpaceToken(" "),
            UnknownToken("b")
        )

        val tokenizer = Tokenizer()
        val whiteSpaceTokenizer = WhiteSpaceTokenizer()
        val actualTokens = whiteSpaceTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun spaceAtTextEnd()
    {
        val inputText = "a "
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            WhiteSpaceToken(" ")
        )

        val tokenizer = Tokenizer()
        val whiteSpaceTokenizer = WhiteSpaceTokenizer()
        val actualTokens = whiteSpaceTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }
}
