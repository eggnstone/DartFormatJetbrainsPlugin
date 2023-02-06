package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TokenizeComments
{
    @Test
    fun combination()
    {
        val inputText = "//end of line comment\n/*multi line comment*/)a "
        val expectedTokens = arrayListOf(
            EndOfLineCommentToken("end of line comment\n"),
            MultiLineCommentToken("multi line comment"),
            UnknownToken(")a ")
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
