package com.eggnstone.jetbrainsplugins.dartformat.tokenizer.stringTokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.StringTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.StringToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestTokenizeParametrized(private val delim1: String, private val delim2: String, @Suppress("UNUSED_PARAMETER") newLineName: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{2}")
        fun data() = arrayOf(
            arrayOf("\"", "'", "Normal quotes"),
            arrayOf("'", "\"", "Apostrophe")
        )
    }

    @Test
    fun stringAtTextStart()
    {
        val inputText = "${delim1}a string$delim1 and then the text continues."
        val expectedTokens = arrayListOf(
            StringToken("${delim1}a string$delim1"),
            UnknownToken(" and then the text continues.")
        )

        val actualTokens = StringTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun stringAtTextMiddle()
    {
        val inputText = "Some text and then ${delim1}a string$delim1 and then the text continues."
        val expectedTokens = arrayListOf(
            UnknownToken("Some text and then "),
            StringToken("${delim1}a string$delim1"),
            UnknownToken(" and then the text continues.")
        )

        val actualTokens = StringTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun stringAtTextEnd()
    {
        val inputText = "Some text and then ${delim1}a string$delim1"
        val expectedTokens = arrayListOf(
            UnknownToken("Some text and then "),
            StringToken("${delim1}a string$delim1")
        )

        val actualTokens = StringTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun stringAtTextEndNotClosed()
    {
        val inputText = "Some text and then ${delim1}a string"
        val expectedTokens = arrayListOf(
            UnknownToken("Some text and then "),
            StringToken("${delim1}a string", isClosed = false)
        )

        val actualTokens = StringTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun stringWithOtherDelimiterToo()
    {
        val inputText = "Some text and then ${delim1}a string using ${delim2}the other delimiter${delim2}, too$delim1 and then the text continues."
        val expectedTokens = arrayListOf(
            UnknownToken("Some text and then "),
            StringToken("${delim1}a string using ${delim2}the other delimiter${delim2}, too$delim1"),
            UnknownToken(" and then the text continues.")
        )

        val actualTokens = StringTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun ignoreEscapedDelimiter()
    {
        val inputText = "Some text and then an escaped \\${delim1} delimiter."
        val expectedTokens = arrayListOf(
            UnknownToken("Some text and then an escaped \\${delim1} delimiter.")
        )

        val actualTokens = StringTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun ignoreEscapedDelimiterInsideDelim1()
    {
        val inputText = "Some text and then ${delim1}a \\${delim1} string$delim1 and then the text continues."
        val expectedTokens = arrayListOf(
            UnknownToken("Some text and then "),
            StringToken("${delim1}a \\${delim1} string$delim1"),
            UnknownToken(" and then the text continues.")
        )

        val actualTokens = StringTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun ignoreEscapedDelimiterInsideDelim2()
    {
        val inputText = "Some text and then ${delim2}a \\${delim1} string$delim2 and then the text continues."
        val expectedTokens = arrayListOf(
            UnknownToken("Some text and then "),
            StringToken("${delim2}a \\${delim1} string$delim2"),
            UnknownToken(" and then the text continues.")
        )

        val actualTokens = StringTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }
}
