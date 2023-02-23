package com.eggnstone.jetbrainsplugins.dartformat.indenter

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class IndentTestsParametrizedWithKeywords(private val keyword: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = TestParams.keywords
    }

    @Test
    fun indentAfterKeywordAfterKeyword()
    {
        val inputTokens = arrayListOf(
            KeywordToken(keyword),
            LineBreakToken("\n"),
            KeywordToken(keyword),
            LineBreakToken("\n"),
            UnknownToken("text"),
            SpecialToken(";")
        )
        val expectedOutputText = "$keyword\n" +
        "    $keyword\n" +
        "        text;"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun closingBraceShouldDecreaseIndentationAtTextEnd()
    {
        val inputTokens = arrayListOf(
            UnknownToken("void main"),
            SpecialToken("("),
            SpecialToken(")"),
            LineBreakToken("\n"),
            SpecialToken("{"),
            LineBreakToken("\n"),
            UnknownToken("runApp"),
            SpecialToken("("),
            UnknownToken("const MyApp"),
            SpecialToken("("),
            SpecialToken(")"),
            SpecialToken(")"),
            SpecialToken(";"),
            LineBreakToken("\n"),
            SpecialToken("}")
        )
        val expectedOutputText = "void main()\n" +
        "{\n" +
        "    runApp(const MyApp());\n" +
        "}"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun doNotIndentEmptyLines()
    {
        val inputTokens = arrayListOf(
            UnknownToken("Text0"), LineBreakToken("\n"),
            SpecialToken.OPENING_CURLY_BRACKET, LineBreakToken("\n"),
            UnknownToken("Text1"), LineBreakToken("\n"),
            LineBreakToken("\n"),
            UnknownToken("Text2"), LineBreakToken("\n")
        )
        val expectedOutputText =
            "Text0\n" +
            "{\n" +
            "    Text1\n" +
            "\n" +
            "    Text2\n"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
