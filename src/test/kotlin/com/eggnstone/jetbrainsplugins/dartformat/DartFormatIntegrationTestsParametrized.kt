package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.config.DartFormatConfig
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class DartFormatIntegrationTestsParametrized(private val newLine: String, @Suppress("UNUSED_PARAMETER") newLineName: String)
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
    fun testEndOfLineCommentAtTextMiddle()
    {
        val inputText = "abc//this is an end of line comment${newLine}def"
        val expectedOutputTokens = arrayListOf<IToken>(
            UnknownToken("abc"),
            EndOfLineCommentToken("this is an end of line comment"),
            LineBreakToken(newLine),
            UnknownToken("def")
        )

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter(DartFormatConfig(true)).format(inputTokens)
        val actualOutputText = Indenter().recreateForIntegrationsTestsOnly(actualOutputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
        assertThat(actualOutputText, equalTo(inputText))
    }
}
