package com.eggnstone.jetbrainsplugins.dartformat.tokenizers.whiteSpaceTokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.WhiteSpaceTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeTests
{
    @Test
    fun space_atTextStart()
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
    fun space_atTextMiddle()
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
    fun twoSpaces_atTextMiddle()
    {
        val inputText = "a  b"
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            WhiteSpaceToken("  "),
            UnknownToken("b")
        )

        val actualTokens = WhiteSpaceTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun space_atTextEnd()
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
