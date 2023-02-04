package com.eggnstone.jetbrainsplugins.dartformat.formatter

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class RemoveUnnecessaryCommas
{
    @Test
    fun removeUnnecessaryCommas()
    {
        val input = ",)"
        val expectedOutput = ")"

        val actualOutput = Formatter.format(input)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun removeUnnecessaryCommas3()
    {
        val input = ",,,)"
        val expectedOutput = ")"

        val actualOutput = Formatter.format(input)

        assertEquals(expectedOutput, actualOutput)
    }
}
