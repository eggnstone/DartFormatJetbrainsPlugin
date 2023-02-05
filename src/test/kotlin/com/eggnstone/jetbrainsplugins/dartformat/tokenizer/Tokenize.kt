package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class Tokenize
{
    @Test
    fun combination()
    {
        val inputText = "//end of line comment\n/*multi line comment*/)a "
        val expectedTokens = arrayListOf(
            EndOfLineCommentToken("end of line comment\n"),
            MultiLineCommentToken("multi line comment"),
            SpecialToken(")"),
            TextToken("a"),
            WhiteSpaceToken(" ")
        )

        val tokenizer = Tokenizer()
        val actualTokens = tokenizer.tokenize(inputText)
        val actualText = tokenizer.recreate(actualTokens)

        assertThat(actualTokens, equalTo(expectedTokens))
        assertThat(actualText, equalTo(inputText))
    }
}
