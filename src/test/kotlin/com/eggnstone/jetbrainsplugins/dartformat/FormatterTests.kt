package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.formatter.Formatter
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class FormatterTests
{
    @Test
    fun testRemoveUnnecessaryCommas()
    {
        val input = ",)"
        val expectedOutput = ")"

        val actualOutput = Formatter.format(input)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testRemoveUnnecessaryCommas3()
    {
        val input = ",,,)"
        val expectedOutput = ")"

        val actualOutput = Formatter.format(input)

        assertEquals(expectedOutput, actualOutput)
    }
}
