package com.eggnstone.jetbrainsplugins.dartformat.integration

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.MasterSplitter
import org.junit.Test
import java.io.File

class AngleBracketsIgnoredTests
{
    @Test
    fun wordPressTools()
    {
        val inputText = File(IntegrationTests.testDataPath + "AngleBracketsIgnored.input.dart").readText()
        val expectedOutputText = File(IntegrationTests.testDataPath + "AngleBracketsIgnored.expected_output.dart").readText()

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }
}
