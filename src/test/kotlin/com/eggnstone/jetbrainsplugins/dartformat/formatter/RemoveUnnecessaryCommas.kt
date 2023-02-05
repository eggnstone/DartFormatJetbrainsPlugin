/*
package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.Tokenizer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RemoveUnnecessaryCommas
{
    @Test
    fun removeUnnecessaryComma()
    {
        val input = ",)"
        val expectedOutput = ")"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommas()
    {
        val input = ",,,)"
        val expectedOutput = ")"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommaTwice()
    {
        val input = ",),)"
        val expectedOutput = "))"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommasWithNewLineN()
    {
        val input = ",\n)"
        val expectedOutput = "\n)"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommasWithNewLineNR()
    {
        val input = ",\n\r)"
        val expectedOutput = "\n\r)"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommasWithNewLineR()
    {
        val input = ",\r)"
        val expectedOutput = "\r)"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommasWithNewLineRN()
    {
        val input = ",\r\n)"
        val expectedOutput = "\r\n)"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommasWithNewLineAndSpace()
    {
        val input = ",\n )"
        val expectedOutput = "\n )"

        val tokens = Tokenizer().tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }
}
*/
