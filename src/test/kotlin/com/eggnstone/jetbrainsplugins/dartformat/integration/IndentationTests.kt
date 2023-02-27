package com.eggnstone.jetbrainsplugins.dartformat.integration

import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.MasterSplitter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class IndentationTests
{
    @Test
    fun multipleBracketsOnlyIndentOnce()
    {
        val inputText = "abc({\n" +
        "def;\n" +
        "})"

        val expectedOutputText = "abc({\n" +
        "    def;\n" +
        "})"

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun keywordIndentsExpression()
    {
        val inputText = "if()\n" +
        "abc;"

        val expectedOutputText = "if()\n" +
        "    abc;"

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun keywordIndentsExpressionWithAngleBrackets()
    {
        val inputText = "if()\n" +
        "abc<>;"

        val expectedOutputText = "if()\n" +
        "    abc<>;"

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun keywordIndentsExpressionWithRoundBrackets()
    {
        val inputText = "if()\n" +
        "abc();"

        val expectedOutputText = "if()\n" +
        "    abc();"

        val result = MasterSplitter().split(inputText)
        PartTools.printParts(result.parts)

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
