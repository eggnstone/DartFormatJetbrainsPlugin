package com.eggnstone.jetbrainsplugins.dartformat

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import org.junit.Ignore
import org.junit.Test
import java.io.File

class DartFormatIntegrationSetStateTests
{
    private val testDataPath = "src/test/kotlin/com/eggnstone/jetbrainsplugins/dartformat/data/"

    @Test
    fun setState_singleLine()
    {
        val inputText = File(testDataPath + "setState_singleLine.input.dart").readText()
        val expectedOutputText = File(testDataPath + "setState_singleLine.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun setStateWithArrow_singleLine()
    {
        val inputText = File(testDataPath + "setStateWithArrow_singleLine.input.dart").readText()
        val expectedOutputText = File(testDataPath + "setStateWithArrow_singleLine.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun setState_multipleLines()
    {
        val inputText = File(testDataPath + "setState_multipleLines.input.dart").readText()
        val expectedOutputText = File(testDataPath + "setState_multipleLines.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    @Ignore
    fun setStateWithArrow_multipleLines()
    {
        val inputText = File(testDataPath + "setStateWithArrow_multipleLines.input.dart").readText()
        val expectedOutputText = File(testDataPath + "setStateWithArrow_multipleLines.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }
}
