package com.eggnstone.jetbrainsplugins.dartformat.integration

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import org.junit.Test
import java.io.File

class MainClassKeyWordTests
{
    @Test
    fun normalClass()
    {
        val inputText = File(IntegrationTests.testDataPath + "NormalClass.input.dart").readText()
        val expectedOutputText = File(IntegrationTests.testDataPath + "NormalClass.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun normalClassWithOpeningCurlyBracketAtSameLine()
    {
        val inputText = File(IntegrationTests.testDataPath + "NormalClassWithOpeningCurlyBracketAtSameLine.input.dart").readText()
        val expectedOutputText = File(IntegrationTests.testDataPath + "NormalClassWithOpeningCurlyBracketAtSameLine.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun normalClassWithMixin()
    {
        val inputText = File(IntegrationTests.testDataPath + "NormalClassWithMixin.input.dart").readText()
        val expectedOutputText = File(IntegrationTests.testDataPath + "NormalClassWithMixin.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun abstractClass()
    {
        val inputText = File(IntegrationTests.testDataPath + "AbstractClass.input.dart").readText()
        val expectedOutputText = File(IntegrationTests.testDataPath + "AbstractClass.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun abstractClassWithOpeningCurlyBracketAtSameLine()
    {
        val inputText = File(IntegrationTests.testDataPath + "AbstractClassWithOpeningCurlyBracketAtSameLine.input.dart").readText()
        val expectedOutputText = File(IntegrationTests.testDataPath + "AbstractClassWithOpeningCurlyBracketAtSameLine.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun abstractClassWithMixin()
    {
        val inputText = File(IntegrationTests.testDataPath + "AbstractClassWithMixin.input.dart").readText()
        val expectedOutputText = File(IntegrationTests.testDataPath + "AbstractClassWithMixin.expected_output.dart").readText()

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }
}
