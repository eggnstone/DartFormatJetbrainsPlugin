package com.eggnstone.jetbrainsplugins.dartformat.indenter

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class IndentTestsParametrizedWithLineBreaks(private val lineBreak: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
    }

    @Test
    fun closingBraceShouldDecreaseIndentationAtTextMiddle()
    {
        val inputTokens = mutableListOf(
            UnknownToken("void main"),
            SpecialToken("("),
            SpecialToken(")"),
            LineBreakToken(lineBreak),
            SpecialToken("{"),
            LineBreakToken(lineBreak),
            UnknownToken("runApp"),
            SpecialToken("("),
            UnknownToken("const MyApp"),
            SpecialToken("("),
            SpecialToken(")"),
            SpecialToken(")"),
            SpecialToken(";"),
            LineBreakToken(lineBreak),
            SpecialToken("}"),
            LineBreakToken(lineBreak),
            UnknownToken("END")
        )
        val expectedOutputText = "void main()$lineBreak" +
            "{$lineBreak" +
            "    runApp(const MyApp());$lineBreak" +
            "}$lineBreak" +
            "END"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun closingBraceShouldDecreaseIndentationAtTextEnd()
    {
        val inputTokens = mutableListOf(
            UnknownToken("void main"),
            SpecialToken("("),
            SpecialToken(")"),
            LineBreakToken(lineBreak),
            SpecialToken("{"),
            LineBreakToken(lineBreak),
            UnknownToken("runApp"),
            SpecialToken("("),
            UnknownToken("const MyApp"),
            SpecialToken("("),
            SpecialToken(")"),
            SpecialToken(")"),
            SpecialToken(";"),
            LineBreakToken(lineBreak),
            SpecialToken("}")
        )
        val expectedOutputText = "void main()$lineBreak" +
            "{$lineBreak" +
            "    runApp(const MyApp());$lineBreak" +
            "}"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun doNotIndentEmptyLines()
    {
        val inputTokens = mutableListOf(
            UnknownToken("Text"), LineBreakToken(lineBreak),
            SpecialToken.OPENING_CURLY_BRACKET, LineBreakToken(lineBreak),
            UnknownToken("Text"), LineBreakToken(lineBreak),
            LineBreakToken(lineBreak),
            UnknownToken("Text"), LineBreakToken(lineBreak)
        )
        val expectedOutputText = "Text$lineBreak" +
            "{$lineBreak" +
            "    Text$lineBreak" +
            lineBreak +
            "    Text$lineBreak"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
