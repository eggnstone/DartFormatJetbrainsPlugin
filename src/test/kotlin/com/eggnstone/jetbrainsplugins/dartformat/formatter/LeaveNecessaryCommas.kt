/*
package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.Tokenizer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class LeaveNecessaryCommas
{
    @Test
    fun leaveNecessaryCommaWithText()
    {
        val input = ",x"
        val expectedOutput = ",x"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun leaveNecessaryCommaWithSpaceAndText()
    {
        val input = ", x"
        val expectedOutput = ", x"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun leaveNecessaryCommaWithNewLineAndText()
    {
        val input = ",\nx"
        val expectedOutput = ",\nx"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun leaveNecessaryCommaWithTextAndBracket()
    {
        val input = ",x)"
        val expectedOutput = ",x)"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }
}
*/
