package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeEndOfLineComment
{
    @Test
    fun endOfLineCommentAtTextStart()
    {
        val input = "//this is a comment //still the same comment\ndef"
        val expectedTokens = arrayListOf(
            EndOfLineCommentToken("this is a comment //still the same comment\n"),
            TextToken("def")
        )

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(actualTokens, equalTo(expectedTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertThat(actualText, equalTo(input))
    }

    @Test
    fun endOfLineCommentInTextMiddle()
    {
        val input = "abc//this is a comment //still the same comment\ndef"
        val expectedTokens = arrayListOf(
            TextToken("abc"),
            EndOfLineCommentToken("this is a comment //still the same comment\n"),
            TextToken(
                "def"
            )
        )

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(actualTokens, equalTo(expectedTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertThat(actualText, equalTo(input))
    }

    @Test
    fun endOfLineCommentAtTextEnd()
    {
        val input = "abc//this is a comment //still the same comment"
        val expectedTokens = arrayListOf(
            TextToken("abc"),
            EndOfLineCommentToken("this is a comment //still the same comment")
        )

        val actualTokens = Tokenizer.tokenize(input)
        assertThat(actualTokens, equalTo(expectedTokens))

        val actualText = Tokenizer.recreate(actualTokens)
        assertThat(actualText, equalTo(input))
    }
}
