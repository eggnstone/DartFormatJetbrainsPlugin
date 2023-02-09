package com.eggnstone.jetbrainsplugins.dartformat.tokenizer.commentTokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.CommentTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeEndOfLineCommentsTests
{
    @Test
    fun endOfLineCommentAtTextStart()
    {
        val inputText = "//comment"
        val expectedTokens = arrayListOf(
            EndOfLineCommentToken("comment")
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun endOfLineCommentAtTextEnd()
    {
        val inputText = "abc//comment"
        val expectedTokens = arrayListOf(
            UnknownToken("abc"),
            EndOfLineCommentToken("comment")
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }
}
