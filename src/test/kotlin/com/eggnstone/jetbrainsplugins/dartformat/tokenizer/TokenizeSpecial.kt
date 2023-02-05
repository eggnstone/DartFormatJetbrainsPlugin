package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeSpecial
{
    @Test
    fun specialCharAtTextStart()
    {
        val inputText = ":b,c;d(e{f[g]h}i)j"
        val expectedTokens = arrayListOf(
            SpecialToken.COLON,
            UnknownToken("b"),
            SpecialToken.COMMA,
            UnknownToken("c"),
            SpecialToken.SEMICOLON,
            UnknownToken("d"),
            SpecialToken.OPENING_ROUND_BRACKET,
            UnknownToken("e"),
            SpecialToken.OPENING_ANGLE_BRACKET,
            UnknownToken("f"),
            SpecialToken.OPENING_SQUARE_BRACKET,
            UnknownToken("g"),
            SpecialToken.CLOSING_SQUARE_BRACKET,
            UnknownToken("h"),
            SpecialToken.CLOSING_ANGLE_BRACKET,
            UnknownToken("i"),
            SpecialToken.CLOSING_ROUND_BRACKET,
            UnknownToken("j")
        )

        val tokenizer = Tokenizer()
        val specialTokenizer = SpecialTokenizer()
        val actualTokens = specialTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun specialCharAtTextMiddle()
    {
        val inputText = "a:b,c;d(e{f[g]h}i)j"
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            SpecialToken.COLON,
            UnknownToken("b"),
            SpecialToken.COMMA,
            UnknownToken("c"),
            SpecialToken.SEMICOLON,
            UnknownToken("d"),
            SpecialToken.OPENING_ROUND_BRACKET,
            UnknownToken("e"),
            SpecialToken.OPENING_ANGLE_BRACKET,
            UnknownToken("f"),
            SpecialToken.OPENING_SQUARE_BRACKET,
            UnknownToken("g"),
            SpecialToken.CLOSING_SQUARE_BRACKET,
            UnknownToken("h"),
            SpecialToken.CLOSING_ANGLE_BRACKET,
            UnknownToken("i"),
            SpecialToken.CLOSING_ROUND_BRACKET,
            UnknownToken("j")
        )

        val tokenizer = Tokenizer()
        val specialTokenizer = SpecialTokenizer()
        val actualTokens = specialTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun specialCharAtTextEnd()
    {
        val inputText = "a:b,c;d(e{f[g]h}i)"
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            SpecialToken.COLON,
            UnknownToken("b"),
            SpecialToken.COMMA,
            UnknownToken("c"),
            SpecialToken.SEMICOLON,
            UnknownToken("d"),
            SpecialToken.OPENING_ROUND_BRACKET,
            UnknownToken("e"),
            SpecialToken.OPENING_ANGLE_BRACKET,
            UnknownToken("f"),
            SpecialToken.OPENING_SQUARE_BRACKET,
            UnknownToken("g"),
            SpecialToken.CLOSING_SQUARE_BRACKET,
            UnknownToken("h"),
            SpecialToken.CLOSING_ANGLE_BRACKET,
            UnknownToken("i"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val tokenizer = Tokenizer()
        val specialTokenizer = SpecialTokenizer()
        val actualTokens = specialTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }
}
