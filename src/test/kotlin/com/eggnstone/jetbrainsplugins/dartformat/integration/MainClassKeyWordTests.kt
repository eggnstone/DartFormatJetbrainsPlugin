package com.eggnstone.jetbrainsplugins.dartformat.integration

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.MasterSplitter
import integration.IntegrationConstants
import org.junit.Test
import java.io.File

class MainClassKeyWordTests
{
    @Test
    fun normalClass()
    {
        val inputText = File(IntegrationConstants.testDataPath + "NormalClass.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "NormalClass.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun normalClassWithOpeningCurlyBracketAtSameLine()
    {
        val inputText = File(IntegrationConstants.testDataPath + "NormalClassWithOpeningCurlyBracketAtSameLine.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "NormalClassWithOpeningCurlyBracketAtSameLine.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun normalClassWithMixin()
    {
        val inputText = File(IntegrationConstants.testDataPath + "NormalClassWithMixin.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "NormalClassWithMixin.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun abstractClass()
    {
        val inputText = File(IntegrationConstants.testDataPath + "AbstractClass.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "AbstractClass.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun abstractClassWithOpeningCurlyBracketAtSameLine()
    {
        val inputText = File(IntegrationConstants.testDataPath + "AbstractClassWithOpeningCurlyBracketAtSameLine.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "AbstractClassWithOpeningCurlyBracketAtSameLine.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun abstractClassWithMixin()
    {
        val inputText = File(IntegrationConstants.testDataPath + "AbstractClassWithMixin.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "AbstractClassWithMixin.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }
}
