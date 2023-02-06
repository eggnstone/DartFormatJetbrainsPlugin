package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.StringToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TokenizeStringParametrized(private val delim1: String, private val delim2: String, @Suppress("UNUSED_PARAMETER") newLineName: String)
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

        val tokenizer = Tokenizer()
        val stringTokenizer = StringTokenizer()
        val actualTokens = stringTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
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

        val tokenizer = Tokenizer()
        val stringTokenizer = StringTokenizer()
        val actualTokens = stringTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun stringAtTextEnd()
    {
        val inputText = "Some text and then ${delim1}a string$delim1"
        val expectedTokens = arrayListOf(
            UnknownToken("Some text and then "),
            StringToken("${delim1}a string$delim1")
        )

        val tokenizer = Tokenizer()
        val stringTokenizer = StringTokenizer()
        val actualTokens = stringTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun stringAtTextEndNotClosed()
    {
        val inputText = "Some text and then ${delim1}a string"
        val expectedTokens = arrayListOf(
            UnknownToken("Some text and then "),
            StringToken("${delim1}a string", isClosed = false)
        )

        val tokenizer = Tokenizer()
        val stringTokenizer = StringTokenizer()
        val actualTokens = stringTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
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

        val tokenizer = Tokenizer()
        val stringTokenizer = StringTokenizer()
        val actualTokens = stringTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun ignoreEscapedDelimiter()
    {
        val inputText = "Some text and then an escaped \\${delim1} delimiter."
        val expectedTokens = arrayListOf(
            UnknownToken("Some text and then an escaped \\${delim1} delimiter.")
        )

        val tokenizer = Tokenizer()
        val stringTokenizer = StringTokenizer()
        val actualTokens = stringTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
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

        val tokenizer = Tokenizer()
        val stringTokenizer = StringTokenizer()
        val actualTokens = stringTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
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

        val tokenizer = Tokenizer()
        val stringTokenizer = StringTokenizer()
        val actualTokens = stringTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }
}
