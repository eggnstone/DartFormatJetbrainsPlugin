package com.eggnstone.jetbrainsplugins.dartformat

import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class IndentationIntegrationTests
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

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun keywordIndents()
    {
        val inputText = "if()\n" +
        "abc;"

        val expectedOutputText = "if()\n" +
        "    abc;"

        val inputTokens = Tokenizer().tokenize(inputText)
        val actualOutputTokens = Formatter().format(inputTokens)
        val actualOutputText = Indenter().indent(actualOutputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
