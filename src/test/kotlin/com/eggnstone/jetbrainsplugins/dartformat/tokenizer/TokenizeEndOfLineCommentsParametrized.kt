package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TokenizeEndOfLineCommentsParametrized(private val newLine: String, @Suppress("UNUSED_PARAMETER") newLineName: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = arrayOf(
            arrayOf("\n", "\\n"),
            arrayOf("\n\r", "\\n\\r"),
            arrayOf("\r", "\\r"),
            arrayOf("\r\n", "\\r\\n")
        )
    }

    @Test
    fun multiLineCommentAtTextStart()
    {
        val inputText = "//comment${newLine}def"
        val expectedTokens = arrayListOf(
            EndOfLineCommentToken("comment$newLine"),
            TextToken("def")
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
        val inputText = "abc//comment${newLine}def"
        val expectedTokens = arrayListOf(
            TextToken("abc"),
            EndOfLineCommentToken("comment$newLine"),
            TextToken("def")
        )

        val tokenizer = Tokenizer()
        val commentTokenizer = CommentTokenizer()
        val actualTokens = commentTokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }
}
