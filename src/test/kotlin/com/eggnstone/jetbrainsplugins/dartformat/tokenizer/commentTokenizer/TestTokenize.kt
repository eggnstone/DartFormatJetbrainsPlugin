package com.eggnstone.jetbrainsplugins.dartformat.tokenizer.commentTokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.CommentTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TestTokenize
{
    @Test
    fun endOfLineCommentBeforeMultiLineComment()
    {
        val inputText = "a//end of line comment/*multi line comment*/a"
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            EndOfLineCommentToken("end of line comment/*multi line comment*/a")
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun multiLineCommentBeforeEndOfLineComment()
    {
        val inputText = "a/*multi line comment*///end of line comment"
        val expectedTokens = arrayListOf(
            UnknownToken("a"),
            MultiLineCommentToken("multi line comment"),
            EndOfLineCommentToken("end of line comment")
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun multiLineCommentInEndOfLineComment()
    {
        val inputText = "//comment/*still comment*/"
        val expectedTokens = arrayListOf(
            EndOfLineCommentToken("comment/*still comment*/")
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun endOfLineCommentInMultiLineComment()
    {
        val inputText = "/*comment //comment*/"
        val expectedTokens = arrayListOf(
            MultiLineCommentToken("comment //comment")
        )

        val actualTokens = CommentTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }
}
