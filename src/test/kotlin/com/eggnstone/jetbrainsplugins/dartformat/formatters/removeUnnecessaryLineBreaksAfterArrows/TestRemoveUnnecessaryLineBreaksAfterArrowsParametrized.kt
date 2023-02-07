package com.eggnstone.jetbrainsplugins.dartformat.formatters.removeUnnecessaryLineBreaksAfterArrows

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.formatters.RemoveUnnecessaryLineBreaksAfterArrows
import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestRemoveUnnecessaryLineBreaksAfterArrowsParametrized(private val newLine: String, @Suppress("UNUSED_PARAMETER") newLineName: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
    }

    @Test
    fun removeUnnecessaryLineBreakWithoutWhiteSpaceBeforeLineBreak()
    {
        val inputTokens = arrayListOf(
            UnknownToken("a"),
            WhiteSpaceToken(" "),
            SpecialToken.ARROW,
            LineBreakToken(newLine),
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

        val actualOutputTokens = RemoveUnnecessaryLineBreaksAfterArrows().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryLineBreakWithWhiteSpaceBeforeLineBreak()
    {
        val inputTokens = arrayListOf(
            UnknownToken("a"),
            WhiteSpaceToken(" "),
            SpecialToken.ARROW,
            WhiteSpaceToken("    "),
            LineBreakToken(newLine),
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

        val actualOutputTokens = RemoveUnnecessaryLineBreaksAfterArrows().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }
}
