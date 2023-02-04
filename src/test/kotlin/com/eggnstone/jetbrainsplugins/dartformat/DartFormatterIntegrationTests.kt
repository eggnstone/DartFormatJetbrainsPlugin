package com.eggnstone.jetbrainsplugins.dartformat

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File

class DartFormatterIntegrationTests
{
    private val TEST_DATA_PATH = "src/test/kotlin/com/eggnstone/jetbrainsplugins/dartformat/data/"

    @Test
    fun testDefaultFlutterMainDart()
    {
        val input = File(TEST_DATA_PATH + "default_flutter_main.input.dart").readText()
        val expectedOutput = File(TEST_DATA_PATH + "default_flutter_main.expected_output.dart").readText()

        val actualOutput = DartFormatter.format(input)

        assertEquals(expectedOutput, actualOutput)
    }
}
