package com.eggnstone.jetbrainsplugins.dartformat.integration

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import org.junit.Ignore
import org.junit.Test
import java.io.File

class DefaultFlutterMainTests
{
    @Test
    fun defaultFlutterMain()
    {
        val inputText = File(IntegrationTests.testDataPath + "default_flutter_main.input.dart").readText()
        val expectedOutputText = File(IntegrationTests.testDataPath + "default_flutter_main.expected_output.dart").readText()

        /*val simpleBlocks = SimpleBlockifier().blockify(inputText)
        SimpleBlockifier().printBlocks(simpleBlocks)*/

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    @Ignore
    fun defaultFlutterMain2()
    {
        val inputText = File(IntegrationTests.testDataPath + "default_flutter_main.input.dart").readText()
        val expectedOutputText = File(IntegrationTests.testDataPath + "default_flutter_main2.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }
}
