package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.config.DartFormatConfig
import com.eggnstone.jetbrainsplugins.dartformat.formatter.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.Tokenizer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.File

class FormatterIntegrationTests
{
    private val TEST_DATA_PATH = "src/test/kotlin/com/eggnstone/jetbrainsplugins/dartformat/formatter/data/"

    //@Ignore("For the future ...")
    @Test
    fun testDefaultFlutterMainDart()
    {
        val inputText = File(TEST_DATA_PATH + "default_flutter_main.input.dart").readText()
        val expectedOutputText = File(TEST_DATA_PATH + "default_flutter_main.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter(DartFormatConfig(true)).format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
