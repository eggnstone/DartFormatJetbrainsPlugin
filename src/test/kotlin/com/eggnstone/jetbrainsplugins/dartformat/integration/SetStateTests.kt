package com.eggnstone.jetbrainsplugins.dartformat.integration

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.MasterSplitter
import integration.IntegrationConstants
import org.junit.Ignore
import org.junit.Test
import java.io.File

class SetStateTests
{
    @Test
    fun setState_singleLine()
    {
        val inputText = File(IntegrationConstants.testDataPath + "setState_singleLine.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "setState_singleLine.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun setStateWithArrow_singleLine()
    {
        val inputText = File(IntegrationConstants.testDataPath + "setStateWithArrow_singleLine.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "setStateWithArrow_singleLine.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    fun setState_multipleLines()
    {
        val inputText = File(IntegrationConstants.testDataPath + "setState_multipleLines.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "setState_multipleLines.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }

    @Test
    @Ignore
    fun setStateWithArrow_multipleLines()
    {
        val inputText = File(IntegrationConstants.testDataPath + "setStateWithArrow_multipleLines.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "setStateWithArrow_multipleLines.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }
}
