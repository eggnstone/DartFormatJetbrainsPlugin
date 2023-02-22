package com.eggnstone.jetbrainsplugins.dartformat.formatters.removeLineBreaksAfterArrows

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.formatters.RemoveLineBreaksAfterArrows
import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class RemoveLineBreaksAfterArrowsTests
{
    @Test
    fun removeLineBreakWithoutWhiteSpaceBeforeLineBreak()
    {
        val inputTokens = arrayListOf(
            UnknownToken("a"),
            WhiteSpaceToken(" "),
            SpecialToken.ARROW,
            LineBreakToken("\n"),
            WhiteSpaceToken("    "),
            UnknownToken("a")
        )
        val expectedOutputTokens = arrayListOf(
            UnknownToken("a"),
            WhiteSpaceToken(" "),
            SpecialToken.ARROW,
            WhiteSpaceToken(" "),
            UnknownToken("a")
        )

        val actualOutputTokens = RemoveLineBreaksAfterArrows().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeLineBreakWithWhiteSpaceBeforeLineBreak()
    {
        val inputTokens = arrayListOf(
            UnknownToken("a"),
            WhiteSpaceToken(" "),
            SpecialToken.ARROW,
            WhiteSpaceToken("    "),
            LineBreakToken("\n"),
            WhiteSpaceToken("    "),
            UnknownToken("a")
        )
        val expectedOutputTokens = arrayListOf(
            UnknownToken("a"),
            WhiteSpaceToken(" "),
            SpecialToken.ARROW,
            WhiteSpaceToken(" "),
            UnknownToken("a")
        )

        val actualOutputTokens = RemoveLineBreaksAfterArrows().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }
}
