package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.Tokenizer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RemoveUnnecessaryCommas
{
    @Test
    fun removeUnnecessaryCommas()
    {
        val input = ",)"
        val expectedOutput = ")"

        val tokens = Tokenizer.tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommas3()
    {
        val input = ",,,)"
        val expectedOutput = ")"

        val tokens = Tokenizer.tokenize(input)
        val actualOutput = Formatter.format(tokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }
}
