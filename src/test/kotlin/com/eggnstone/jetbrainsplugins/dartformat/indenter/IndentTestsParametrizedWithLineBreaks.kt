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
class IndentTestsParametrizedWithLineBreaks(private val newLine: String, @Suppress("UNUSED_PARAMETER") unused: String)
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
            SpecialToken("}"),
            LineBreakToken(newLine),
            UnknownToken("END")
        )
        val expectedOutputText = "void main()$newLine" +
        "{$newLine" +
        "    runApp(const MyApp());$newLine" +
        "}$newLine" +
        "END"

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
            UnknownToken("Text"), LineBreakToken(newLine),
            SpecialToken.OPENING_CURLY_BRACKET, LineBreakToken(newLine),
            UnknownToken("Text"), LineBreakToken(newLine),
            LineBreakToken(newLine),
            UnknownToken("Text"), LineBreakToken(newLine)
        )
        val expectedOutputText = "Text$newLine" +
        "{$newLine" +
        "    Text$newLine" +
        newLine +
        "    Text$newLine"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
