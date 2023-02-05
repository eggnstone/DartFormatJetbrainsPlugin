package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeMultiLineComments
{
    @Test
    fun multiLineCommentAtTextStart()
    {
        val inputText = "/*comment*/def"
        val expectedTokens = arrayListOf(
            MultiLineCommentToken("comment"),
            UnknownToken("def")
        )

        val tokenizer = Tokenizer()
        val commentTokenizer = CommentTokenizer()
        val actualTokens = commentTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun multiLineCommentAtTextMiddle()
    {
        val inputText = "abc/*comment*/def"
        val expectedTokens = arrayListOf(
            UnknownToken("abc"),
            MultiLineCommentToken("comment"),
            UnknownToken("def")
        )

        val tokenizer = Tokenizer()
        val commentTokenizer = CommentTokenizer()
        val actualTokens = commentTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun multiLineCommentAtTextEnd()
    {
        val inputText = "abc/*comment*/"
        val expectedTokens = arrayListOf(
            UnknownToken("abc"),
            MultiLineCommentToken("comment")
        )

        val tokenizer = Tokenizer()
        val commentTokenizer = CommentTokenizer()
        val actualTokens = commentTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun unclosedMultiLineCommentAtTextEnd1Missing()
    {
        val inputText = "abc/*comment*"
        val expectedTokens = arrayListOf(
            UnknownToken("abc"),
            MultiLineCommentToken("comment*", isClosed = false)
        )

        val tokenizer = Tokenizer()
        val commentTokenizer = CommentTokenizer()
        val actualTokens = commentTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun unclosedMultiLineCommentAtTextEnd2Missing()
    {
        val inputText = "abc/*comment"
        val expectedTokens = arrayListOf(
            UnknownToken("abc"),
            MultiLineCommentToken("comment", isClosed = false)
        )

        val tokenizer = Tokenizer()
        val commentTokenizer = CommentTokenizer()
        val actualTokens = commentTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }
}
