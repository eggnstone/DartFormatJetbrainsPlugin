package com.eggnstone.jetbrainsplugins.dartformat.formatter

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

        val actualOutput = Formatter.format(input)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommas3()
    {
        val input = ",,,)"
        val expectedOutput = ")"

        val actualOutput = Formatter.format(input)

        assertThat(actualOutput, equalTo(expectedOutput))
    }
}
