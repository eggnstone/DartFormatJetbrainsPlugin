package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
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
            TextToken("b"),
            SpecialToken.COMMA,
            TextToken("c"),
            SpecialToken.SEMICOLON,
            TextToken("d"),
            SpecialToken.OPENING_BRACKET,
            TextToken("e"),
            SpecialToken.OPENING_ANGLE_BRACKET,
            TextToken("f"),
            SpecialToken.OPENING_SQUARE_BRACKET,
            TextToken("g"),
            SpecialToken.CLOSING_SQUARE_BRACKET,
            TextToken("h"),
            SpecialToken.CLOSING_ANGLE_BRACKET,
            TextToken("i"),
            SpecialToken.CLOSING_BRACKET,
            TextToken("j")
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
            TextToken("a"),
            SpecialToken.COLON,
            TextToken("b"),
            SpecialToken.COMMA,
            TextToken("c"),
            SpecialToken.SEMICOLON,
            TextToken("d"),
            SpecialToken.OPENING_BRACKET,
            TextToken("e"),
            SpecialToken.OPENING_ANGLE_BRACKET,
            TextToken("f"),
            SpecialToken.OPENING_SQUARE_BRACKET,
            TextToken("g"),
            SpecialToken.CLOSING_SQUARE_BRACKET,
            TextToken("h"),
            SpecialToken.CLOSING_ANGLE_BRACKET,
            TextToken("i"),
            SpecialToken.CLOSING_BRACKET,
            TextToken("j")
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
            TextToken("a"),
            SpecialToken.COLON,
            TextToken("b"),
            SpecialToken.COMMA,
            TextToken("c"),
            SpecialToken.SEMICOLON,
            TextToken("d"),
            SpecialToken.OPENING_BRACKET,
            TextToken("e"),
            SpecialToken.OPENING_ANGLE_BRACKET,
            TextToken("f"),
            SpecialToken.OPENING_SQUARE_BRACKET,
            TextToken("g"),
            SpecialToken.CLOSING_SQUARE_BRACKET,
            TextToken("h"),
            SpecialToken.CLOSING_ANGLE_BRACKET,
            TextToken("i"),
            SpecialToken.CLOSING_BRACKET
        )

        val tokenizer = Tokenizer()
        val specialTokenizer = SpecialTokenizer()
        val actualTokens = specialTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }
}
