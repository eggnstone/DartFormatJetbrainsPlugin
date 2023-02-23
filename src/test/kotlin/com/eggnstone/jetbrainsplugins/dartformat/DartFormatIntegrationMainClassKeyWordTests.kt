package com.eggnstone.jetbrainsplugins.dartformat

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import org.junit.Test
import java.io.File

class DartFormatIntegrationMainClassKeyWordTests
{
    private val testDataPath = "src/test/kotlin/com/eggnstone/jetbrainsplugins/dartformat/data/"

    @Test
    fun normalClass()
    {
        val inputText = File(testDataPath + "NormalClass.input.dart").readText()
        val expectedOutputText = File(testDataPath + "NormalClass.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun normalClassWithOpeningCurlyBracketAtSameLine()
    {
        val inputText = File(testDataPath + "NormalClassWithOpeningCurlyBracketAtSameLine.input.dart").readText()
        val expectedOutputText = File(testDataPath + "NormalClassWithOpeningCurlyBracketAtSameLine.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun normalClassWithMixin()
    {
        val inputText = File(testDataPath + "NormalClassWithMixin.input.dart").readText()
        val expectedOutputText = File(testDataPath + "NormalClassWithMixin.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun abstractClass()
    {
        val inputText = File(testDataPath + "AbstractClass.input.dart").readText()
        val expectedOutputText = File(testDataPath + "AbstractClass.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun abstractClassWithOpeningCurlyBracketAtSameLine()
    {
        val inputText = File(testDataPath + "AbstractClassWithOpeningCurlyBracketAtSameLine.input.dart").readText()
        val expectedOutputText = File(testDataPath + "AbstractClassWithOpeningCurlyBracketAtSameLine.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun abstractClassWithMixin()
    {
        val inputText = File(testDataPath + "AbstractClassWithMixin.input.dart").readText()
        val expectedOutputText = File(testDataPath + "AbstractClassWithMixin.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }
}
