package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeComments
{
    @Test
    fun multiLineCommentInEndOfLineComment()
    {
        val inputText = "//comment/*still comment*/"
        val expectedTokens = arrayListOf(
            EndOfLineCommentToken("comment/*still comment*/")
        )

        val tokenizer = Tokenizer()
        val commentTokenizer = CommentTokenizer()
        val actualTokens = commentTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }

    @Test
    fun endOfLineCommentInMultiLineComment()
    {
        val inputText = "/*comment //comment*/"
        val expectedTokens = arrayListOf(
            MultiLineCommentToken("comment //comment")
        )

        val tokenizer = Tokenizer()
        val commentTokenizer = CommentTokenizer()
        val actualTokens = commentTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }
}
