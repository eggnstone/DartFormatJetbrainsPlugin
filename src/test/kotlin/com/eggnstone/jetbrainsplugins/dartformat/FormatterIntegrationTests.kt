package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.formatter.Formatter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Ignore
import org.junit.Test
import java.io.File

class FormatterIntegrationTests
{
    private val TEST_DATA_PATH = "src/test/kotlin/com/eggnstone/jetbrainsplugins/dartformat/formatter/data/"

    @Ignore("For the future ...")
    @Test
    fun testDefaultFlutterMainDart()
    {
        val input = File(TEST_DATA_PATH + "default_flutter_main.input.dart").readText()
        val expectedOutput = File(TEST_DATA_PATH + "default_flutter_main.expected_output.dart").readText()

        val actualOutput = Formatter.format(input)

        assertThat(actualOutput, equalTo(expectedOutput))
    }
}
