package com.eggnstone.jetbrainsplugins.dartformat.tokenizers.specialTokenizer

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.SpecialTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TokenizeTestsParametrized(private val special: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = TestParams.specials
    }

    @Test
    @Ignore
    fun specialCharAtTextStart()
    {
        val inputText = "${special}z"
        val expectedTokens = mutableListOf(
            SpecialToken(special),
            UnknownToken("z")
        )

        val actualTokens = SpecialTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    @Ignore
    fun specialCharAtTextMiddle()
    {
        val inputText = "a${special}z"
        val expectedTokens = mutableListOf(
            UnknownToken("a"),
            SpecialToken(special),
            UnknownToken("z")
        )

        val actualTokens = SpecialTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    @Ignore
    fun specialCharAtTextEnd()
    {
        val inputText = "a${special}"
        val expectedTokens = mutableListOf(
            UnknownToken("a"),
            SpecialToken(special)
        )

        val actualTokens = SpecialTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }
}
