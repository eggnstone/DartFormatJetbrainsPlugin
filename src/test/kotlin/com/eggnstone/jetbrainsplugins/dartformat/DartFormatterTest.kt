package com.eggnstone.jetbrainsplugins.dartformat

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class DartFormatterTest
{
    @org.junit.jupiter.api.BeforeEach
    fun setUp()
    {
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown()
    {
    }

    @Test
    fun testRemoveUnnecessaryCommas()
    {
        val input = ",)"
        val expectedOutput = ")"

        val actualOutput = DartFormatter.format(input)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testRemoveUnnecessaryCommas2()
    {
        val input = ",,)"
        val expectedOutput = ")"

        val actualOutput = DartFormatter.format(input)

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testRemoveUnnecessaryCommas3()
    {
        val input = ",,,)"
        val expectedOutput = ")"

        val actualOutput = DartFormatter.format(input)

        assertEquals(expectedOutput, actualOutput)
    }
}
