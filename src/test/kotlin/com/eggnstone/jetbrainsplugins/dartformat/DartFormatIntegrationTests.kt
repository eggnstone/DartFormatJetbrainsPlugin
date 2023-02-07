package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.config.DartFormatConfig
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import org.junit.Test
import java.io.File

class DartFormatIntegrationTests
{
    private val testDataPath = "src/test/kotlin/com/eggnstone/jetbrainsplugins/dartformat/data/"

    @Test
    fun testDefaultFlutterMainDart()
    {
        val inputText = File(testDataPath + "default_flutter_main.input.dart").readText()
        val expectedOutputText = File(testDataPath + "default_flutter_main.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter(DartFormatConfig(true)).format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        // TODO: setState(() {
        // TODO: dots
        //TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }
}
