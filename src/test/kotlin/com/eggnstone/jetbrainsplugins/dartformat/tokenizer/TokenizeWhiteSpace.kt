/*
package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TokenizeWhiteSpace(private val newline: String, @Suppress("UNUSED_PARAMETER") newlineName: String)
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
    fun newline()
    {
        val input = "a${newline}b"
        val expectedTokens = arrayListOf(TextToken("a"), WhiteSpaceToken(newline), TextToken("b"))

        val tokenizer = Tokenizer()
        val actualTokens = tokenizer.tokenize(input)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(input))
    }
}
*/
