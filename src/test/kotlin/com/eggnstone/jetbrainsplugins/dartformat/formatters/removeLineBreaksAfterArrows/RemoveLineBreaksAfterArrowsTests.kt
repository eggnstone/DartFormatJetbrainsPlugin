package com.eggnstone.jetbrainsplugins.dartformat.formatters.removeLineBreaksAfterArrows

import com.eggnstone.jetbrainsplugins.dartformat.formatters.RemoveLineBreaksAfterArrows
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

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
