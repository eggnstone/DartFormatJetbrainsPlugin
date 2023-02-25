package com.eggnstone.jetbrainsplugins.dartformat.integration

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class EndOfLineTestsParametrized(private val newLine: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
    }

    @Test
    fun testEndOfLineCommentAtTextMiddle()
    {
        val inputText = "abc//this is an end of line comment${newLine}def"
        val expectedOutputTokens = arrayListOf(
            UnknownToken("abc"),
            EndOfLineCommentToken("this is an end of line comment"),
            LineBreakToken(newLine),
            UnknownToken("def")
        )

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().recreate(actualOutputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
        assertThat(actualOutputText, equalTo(inputText))
    }
}
