package com.eggnstone.jetbrainsplugins.dartformat.tokenizers.commentTokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.CommentTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeMultiLineCommentsTests
{
    @Test
    fun multiLineCommentAtTextStart()
    {
        val inputText = "/*comment*/def"
        val expectedTokens = mutableListOf(
            MultiLineCommentToken("comment"),
            UnknownToken("def")
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun multiLineCommentAtTextMiddle()
    {
        val inputText = "abc/*comment*/def"
        val expectedTokens = mutableListOf(
            UnknownToken("abc"),
            MultiLineCommentToken("comment"),
            UnknownToken("def")
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun multiLineCommentAtTextEnd()
    {
        val inputText = "abc/*comment*/"
        val expectedTokens = mutableListOf(
            UnknownToken("abc"),
            MultiLineCommentToken("comment")
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun unclosedMultiLineCommentAtTextEndNotClosedMissing1()
    {
        val inputText = "abc/*comment*"
        val expectedTokens = mutableListOf(
            UnknownToken("abc"),
            MultiLineCommentToken("comment*", isClosed = false)
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun unclosedMultiLineCommentAtTextEndNotClosedMissing2()
    {
        val inputText = "abc/*comment"
        val expectedTokens = mutableListOf(
            UnknownToken("abc"),
            MultiLineCommentToken("comment", isClosed = false)
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }
}
