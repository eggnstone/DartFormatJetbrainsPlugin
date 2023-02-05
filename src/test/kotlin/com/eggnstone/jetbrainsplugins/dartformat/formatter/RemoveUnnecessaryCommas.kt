package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RemoveUnnecessaryCommas
{
    @Test
    fun removeUnnecessaryComma()
    {
        //val input = ",)"
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.CLOSING_BRACKET
        )
        val expectedOutput = ")"

        val actualOutput = Formatter.format(inputTokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommas()
    {
        //val input = ",,,)"
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.COMMA,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_BRACKET
        )
        val expectedOutput = ")"

        val actualOutput = Formatter.format(inputTokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommaTwice()
    {
        //val input = ",),)"
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.CLOSING_BRACKET,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_BRACKET
        )
        val expectedOutput = "))"

        val actualOutput = Formatter.format(inputTokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }
}
