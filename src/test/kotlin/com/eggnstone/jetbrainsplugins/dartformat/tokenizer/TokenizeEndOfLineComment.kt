/*
package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TokenizeEndOfLineComment(private val newline: String, @Suppress("UNUSED_PARAMETER") newlineName: String)
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
    fun endOfLineCommentAtTextStart()
    {
        val input = "//this is a comment //still the same comment${newline}def"
        val expectedTokens = arrayListOf(
            EndOfLineCommentToken("this is a comment //still the same comment${newline}"),
            TextToken("def")
        )

        val tokenizer = Tokenizer()
        val actualTokens = tokenizer.tokenize(input)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(input))
    }

    @Test
    fun endOfLineCommentInTextMiddle()
    {
        val input = "abc//this is a comment //still the same comment${newline}def"
        val expectedTokens = arrayListOf(
            TextToken("abc"),
            EndOfLineCommentToken("this is a comment //still the same comment${newline}"),
            TextToken("def")
        )

        val tokenizer = Tokenizer()
        val actualTokens = tokenizer.tokenize(input)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
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

        val tokenizer = Tokenizer()
        val actualTokens = tokenizer.tokenize(input)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(input))
    }
}
*/
