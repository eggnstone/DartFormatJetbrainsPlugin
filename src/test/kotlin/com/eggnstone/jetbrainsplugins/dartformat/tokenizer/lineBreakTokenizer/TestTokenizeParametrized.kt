package com.eggnstone.jetbrainsplugins.dartformat.tokenizer.lineBreakTokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.LineBreakTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestTokenizeParametrized(private val newLine: String, @Suppress("UNUSED_PARAMETER") newLineName: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = arrayOf(
            arrayOf("\n", "\\n"),
            arrayOf("\n\r", "\\n\\r"),
            arrayOf("\r", "\\r"),
            arrayOf("\r\n", "\\r\\n")
        )
    }

    @Test
    fun newLineAtTextStart()
    {
        val inputText = "${newLine}b"
        val expectedTokens = arrayListOf(
            LineBreakToken(newLine),
            UnknownToken("b")
        )

        val actualTokens = LineBreakTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun newLineAtTextMiddle()
    {
        val inputText = "a${newLine}b"
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            LineBreakToken(newLine),
            UnknownToken("b")
        )

        val actualTokens = LineBreakTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun newLineAtTextMiddle2()
    {
        val inputText = "a${newLine}${newLine}b"
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            LineBreakToken(newLine),
            LineBreakToken(newLine),
            UnknownToken("b")
        )

        val actualTokens = LineBreakTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun newLineAtTextEnd()
    {
        val inputText = "a${newLine}"
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            LineBreakToken(newLine)
        )

        val actualTokens = LineBreakTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }
}
