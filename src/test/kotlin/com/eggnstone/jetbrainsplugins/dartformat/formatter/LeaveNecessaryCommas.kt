package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class LeaveNecessaryCommas
{
    @Test
    fun leaveNecessaryCommaWithText()
    {
        val inputTokens = arrayListOf(
            SpecialToken(","),
            TextToken("x")
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
            TextToken("x")
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(inputTokens))
    }

    @Test
    fun leaveNecessaryCommaWithNewLineAndText()
    {
        val inputTokens = arrayListOf(
            SpecialToken(","),
            WhiteSpaceToken("\n"),
            TextToken("x")
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(inputTokens))
    }

    @Test
    fun leaveNecessaryCommaWithTextAndBracket()
    {
        val inputTokens = arrayListOf(
            SpecialToken(","),
            TextToken("x"),
            SpecialToken(")")
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(inputTokens))
    }
}
