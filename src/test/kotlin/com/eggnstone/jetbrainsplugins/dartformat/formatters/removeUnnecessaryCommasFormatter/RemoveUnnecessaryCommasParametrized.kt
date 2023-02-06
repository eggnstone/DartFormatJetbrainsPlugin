package com.eggnstone.jetbrainsplugins.dartformat.formatters.removeUnnecessaryCommasFormatter

import com.eggnstone.jetbrainsplugins.dartformat.formatters.RemoveUnnecessaryCommasFormatter
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class RemoveUnnecessaryCommasParametrized(private val newLine: String, @Suppress("UNUSED_PARAMETER") newLineName: String)
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
    fun removeUnnecessaryComma()
    {
        //val input = ",$newLine)"
        val inputTokens = arrayListOf(
            SpecialToken.COMMA,
            WhiteSpaceToken(newLine),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "$newLine)"
        val expectedOutputTokens = arrayListOf(
            WhiteSpaceToken(newLine),
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommasWithSpace()
    {
        //val input = ",$newLine )"
        val inputTokens = arrayListOf(
            SpecialToken.COMMA,
            WhiteSpaceToken("$newLine "),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "$newLine )"
        val expectedOutputTokens = arrayListOf(
            WhiteSpaceToken("$newLine "),
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }
}
