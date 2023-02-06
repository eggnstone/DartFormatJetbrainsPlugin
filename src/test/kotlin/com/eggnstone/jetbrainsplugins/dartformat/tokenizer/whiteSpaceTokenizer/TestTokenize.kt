package com.eggnstone.jetbrainsplugins.dartformat.tokenizer.whiteSpaceTokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.WhiteSpaceTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TestTokenize
{
    @Test
    fun spaceAtTextStart()
    {
        val inputText = " b"
        val expectedTokens = arrayListOf(
            WhiteSpaceToken(" "),
            UnknownToken("b")
        )

        val actualTokens = WhiteSpaceTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
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

        val actualTokens = WhiteSpaceTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun spaceAtTextEnd()
    {
        val inputText = "a "
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            WhiteSpaceToken(" ")
        )

        val actualTokens = WhiteSpaceTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }
}
