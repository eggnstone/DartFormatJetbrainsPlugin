package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TokenizeMultiLineComment
{
    @Test
    fun multiLineComment()
    {
        val input = "abc/*this is a comment*/xyz"
        val expectedTokens = arrayListOf(TextToken("abc"), MultiLineToken("this is a comment"), TextToken("xyz"))

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(expectedTokens, equalTo(actualTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertEquals(input, actualText)
    }

    @Test
    fun multiLineCommentAtTextStart()
    {
        val input = "/*this is a comment*/xyz"
        val expectedTokens = arrayListOf(MultiLineToken("this is a comment"), TextToken("xyz"))

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(expectedTokens, equalTo(actualTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertEquals(input, actualText)
    }

    @Test
    fun multiLineCommentAtTextEnd()
    {
        val input = "abc/*this is a comment*/"
        val expectedTokens = arrayListOf(TextToken("abc"), MultiLineToken("this is a comment"))

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(expectedTokens, equalTo(actualTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertEquals(input, actualText)
    }
}
