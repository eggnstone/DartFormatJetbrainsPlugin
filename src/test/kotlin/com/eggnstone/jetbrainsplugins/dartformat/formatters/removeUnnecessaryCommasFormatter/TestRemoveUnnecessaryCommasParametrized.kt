package com.eggnstone.jetbrainsplugins.dartformat.formatters.removeUnnecessaryCommasFormatter

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.formatters.RemoveUnnecessaryCommasFormatter
import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestRemoveUnnecessaryCommasParametrized(private val newLine: String, @Suppress("UNUSED_PARAMETER") newLineName: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
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
    fun removeUnnecessaryCommaWithEndOfLineComment()
    {
        //val input = ",//end of line comment\n)"
        val inputTokens = arrayListOf(
            SpecialToken.COMMA,
            EndOfLineCommentToken("end of line comment"),
            LineBreakToken(newLine),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "//end of line comment\\n)"
        val expectedOutputTokens = arrayListOf(
            EndOfLineCommentToken("end of line comment"),
            LineBreakToken(newLine),
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
