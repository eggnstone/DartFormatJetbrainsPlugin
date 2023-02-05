package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeMultiLineComment
{
    @Test
    fun multiLineCommentAtTextStart()
    {
        val input = "/*this is a comment*/xyz"
        val expectedTokens = arrayListOf(MultiLineCommentToken("this is a comment"), TextToken("xyz"))

        val tokenizer = Tokenizer()
        val actualTokens = tokenizer.tokenize(input)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(input))
    }

    @Test
    fun multiLineCommentInTextMiddle()
    {
        val input = "abc/*this is a comment*/xyz"
        val expectedTokens = arrayListOf(TextToken("abc"), MultiLineCommentToken("this is a comment"), TextToken("xyz"))

        val tokenizer = Tokenizer()
        val actualTokens = tokenizer.tokenize(input)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(input))
    }

    @Test
    fun multiLineCommentAtTextEnd()
    {
        val input = "abc/*this is a comment*/"
        val expectedTokens = arrayListOf(TextToken("abc"), MultiLineCommentToken("this is a comment"))

        val tokenizer = Tokenizer()
        val actualTokens = tokenizer.tokenize(input)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(input))
    }
}
