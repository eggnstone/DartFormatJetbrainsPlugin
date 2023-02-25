package com.eggnstone.jetbrainsplugins.dartformat.formatters.removeLineBreaksAfterArrows

import com.eggnstone.jetbrainsplugins.dartformat.formatters.RemoveLineBreaksAfterArrows
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Ignore
import org.junit.Test

class LeaveLineBreaksAfterArrowsTests
{
    @Test
    @Ignore
    fun leaveLineBreakAfterArrowWhenNewStackNotEmpty()
    {
        val inputTokens = mutableListOf(
            UnknownToken("a"), SpecialToken("("), SpecialToken.ARROW, LineBreakToken("\n"),
            WhiteSpaceToken("  "), UnknownToken("a")
        )
        val expectedOutputTokens = mutableListOf(
            UnknownToken("a"), SpecialToken("("), SpecialToken.ARROW, LineBreakToken("\n"),
            WhiteSpaceToken("    "), UnknownToken("a")
        )

        val actualOutputTokens = RemoveLineBreaksAfterArrows().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }
}
