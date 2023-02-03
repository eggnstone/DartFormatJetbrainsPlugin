package com.eggnstone.jetbrainsplugins.dartformat

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

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
        val input = "const X x = X(1, 2,);"
        val expectedOutput = "const X x = X(1, 2);"

        val actualOutput = DartFormatter.format(input)

        assertEquals(expectedOutput, actualOutput)
    }
}