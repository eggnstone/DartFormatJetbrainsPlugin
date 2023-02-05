package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
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
            TextToken("b")
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
            TextToken("a"),
            WhiteSpaceToken(" "),
            TextToken("b")
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
            TextToken("a"),
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
