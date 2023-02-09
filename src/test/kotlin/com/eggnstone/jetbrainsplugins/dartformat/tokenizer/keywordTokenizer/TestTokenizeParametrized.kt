package com.eggnstone.jetbrainsplugins.dartformat.tokenizer.keywordTokenizer

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.KeywordTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.KeywordToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestTokenizeParametrized(private val keyword: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = TestParams.keywords
    }

    @Test
    fun keywordAtTextStart()
    {
        val inputText = "$keyword xyz"
        val expectedTokens = arrayListOf(
                KeywordToken(keyword),
                UnknownToken(" xyz")
        )

        val actualTokens = KeywordTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun keywordAtTextMiddle()
    {
        val inputText = "abc $keyword xyz"
        val expectedTokens = arrayListOf(
                UnknownToken("abc "),
                KeywordToken(keyword),
                UnknownToken(" xyz")
        )

        val actualTokens = KeywordTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun keywordAtTextEnd()
    {
        val inputText = "abc $keyword"
        val expectedTokens = arrayListOf(
                UnknownToken("abc "),
                KeywordToken(keyword)
        )

        val actualTokens = KeywordTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    // TODO: parametrize to test all combinations of keywords * non-keyword-chars at start * non-keyword-chars at end
    @Test
    fun acceptKeywordWhenPrecededByBracket()
    {
        val inputText = "($keyword)"
        val expectedTokens = arrayListOf(
                UnknownToken("("),
                KeywordToken(keyword),
                UnknownToken(")")
        )

        val actualTokens = KeywordTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun leaveKeyWordWhenPrecededByUnderscore()
    {
        val inputText = "_$keyword)"
        val expectedTokens = arrayListOf(
                UnknownToken("_$keyword)")
        )

        val actualTokens = KeywordTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }
}
