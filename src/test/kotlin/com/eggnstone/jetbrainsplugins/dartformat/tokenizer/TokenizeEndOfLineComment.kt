package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeEndOfLineComment
{
    @Test
    fun endOfLineComment()
    {
        val input = "abc//this is a comment //xyz"
        val expectedTokens = arrayListOf(TextToken("abc"), EndOfLineCommentToken("this is a comment //xyz"))

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(actualTokens, equalTo(expectedTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertThat(actualText, equalTo(input))
    }
}
