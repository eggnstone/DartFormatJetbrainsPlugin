package com.eggnstone.jetbrainsplugins.dartformat.formatters.removeUnnecessaryCommasFormatter

import com.eggnstone.jetbrainsplugins.dartformat.formatters.RemoveUnnecessaryCommasFormatter
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class LeaveNecessaryCommasTests
{
    @Test
    fun leaveNecessaryCommaWithText()
    {
        val inputTokens = arrayListOf(
            SpecialToken(","),
            UnknownToken("x")
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(inputTokens))
    }

    @Test
    fun leaveNecessaryCommaWithSpaceAndText()
    {
        val inputTokens = arrayListOf(
            SpecialToken(","),
            WhiteSpaceToken(" "),
            UnknownToken("x")
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(inputTokens))
    }

    @Test
    fun leaveNecessaryCommaWithNewLineAndText()
    {
        val inputTokens = arrayListOf(
            SpecialToken(","),
            LineBreakToken("\n"),
            UnknownToken("x")
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(inputTokens))
    }

    @Test
    fun leaveNecessaryCommaWithTextAndBracket()
    {
        val inputTokens = arrayListOf(
            SpecialToken(","),
            UnknownToken("x"),
            SpecialToken(")")
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(inputTokens))
    }
}
