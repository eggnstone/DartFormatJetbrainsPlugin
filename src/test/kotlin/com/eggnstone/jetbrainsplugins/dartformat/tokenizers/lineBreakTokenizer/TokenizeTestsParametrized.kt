package com.eggnstone.jetbrainsplugins.dartformat.tokenizers.lineBreakTokenizer

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.LineBreakTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TokenizeTestsParametrized(private val lineBreak: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
    }

    @Test
    fun newLineAtTextStart()
    {
        val inputText = "${lineBreak}b"
        val expectedTokens = mutableListOf(
            LineBreakToken(lineBreak),
            UnknownToken("b")
        )

        val actualTokens = LineBreakTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun lineBreakAtTextMiddle()
    {
        val inputText = "a${lineBreak}b"
        val expectedTokens = mutableListOf(
            UnknownToken("a"),
            LineBreakToken(lineBreak),
            UnknownToken("b")
        )

        val actualTokens = LineBreakTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun lineBreakAtTextMiddle2()
    {
        val inputText = "a${lineBreak}${lineBreak}b"
        val expectedTokens = mutableListOf(
            UnknownToken("a"),
            LineBreakToken(lineBreak),
            LineBreakToken(lineBreak),
            UnknownToken("b")
        )

        val actualTokens = LineBreakTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun lineBreakAtTextEnd()
    {
        val inputText = "a${lineBreak}"
        val expectedTokens = mutableListOf(
            UnknownToken("a"),
            LineBreakToken(lineBreak)
        )

        val actualTokens = LineBreakTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }
}
