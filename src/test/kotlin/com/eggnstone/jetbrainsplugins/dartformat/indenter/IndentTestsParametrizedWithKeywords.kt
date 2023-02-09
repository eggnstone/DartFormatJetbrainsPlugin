package com.eggnstone.jetbrainsplugins.dartformat.indenter

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class IndentTestsParametrizedWithKeywords(private val newLine: String, private val keyword: String, @Suppress("UNUSED_PARAMETER") paramName: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{2}")
        fun data() = TestParams.lineBreaksAndKeywords
    }

    @Test
    fun indentAfterKeyword()
    {
        val inputTokens = arrayListOf(
            KeywordToken(keyword),
            LineBreakToken(newLine),
            KeywordToken(keyword),
            LineBreakToken(newLine),
            UnknownToken("text"),
            SpecialToken(";")
        )
        val expectedOutputText = "$keyword$newLine" +
            "    $keyword$newLine" +
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
            LineBreakToken(newLine),
            SpecialToken("{"),
            LineBreakToken(newLine),
            UnknownToken("runApp"),
            SpecialToken("("),
            UnknownToken("const MyApp"),
            SpecialToken("("),
            SpecialToken(")"),
            SpecialToken(")"),
            SpecialToken(";"),
            LineBreakToken(newLine),
            SpecialToken("}")
        )
        val expectedOutputText = "void main()$newLine" +
            "{$newLine" +
            "    runApp(const MyApp());$newLine" +
            "}"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun doNotIndentEmptyLines()
    {
        val inputTokens = arrayListOf(
            UnknownToken("Text"), WhiteSpaceToken(" "), SpecialToken.OPENING_ANGLE_BRACKET, LineBreakToken(newLine),
            WhiteSpaceToken("    "), UnknownToken("Text"), LineBreakToken(newLine),
            LineBreakToken(newLine),
            WhiteSpaceToken("    "), UnknownToken("Text"), LineBreakToken(newLine)
        )
        val expectedOutputText = "Text {$newLine" +
            "    Text$newLine" +
            newLine +
            "    Text$newLine"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
