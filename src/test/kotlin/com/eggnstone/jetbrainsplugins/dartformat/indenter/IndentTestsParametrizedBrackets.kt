package com.eggnstone.jetbrainsplugins.dartformat.indenter

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class IndentTestsParametrizedBrackets(private val openingBracket: String, private val closingBracket: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{2}")
        fun data() = TestParams.brackets
    }

    @Test
    fun brackets_atLineStart()
    {
        val inputTokens = arrayListOf(
            SpecialToken(openingBracket), LineBreakToken("\n"),
            UnknownToken("abc"), SpecialToken(";"), LineBreakToken("\n"),
            SpecialToken(closingBracket)
        )
        val expectedOutputText =
            "$openingBracket\n" +
            "    abc;\n" +
            closingBracket

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        MatcherAssert.assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun bracketsLevel2_atLineStart()
    {
        val inputTokens = arrayListOf(
            SpecialToken(openingBracket), LineBreakToken("\n"),
            SpecialToken(openingBracket), LineBreakToken("\n"),
            UnknownToken("abc"), SpecialToken(";"), LineBreakToken("\n"),
            SpecialToken(closingBracket), LineBreakToken("\n"),
            SpecialToken(closingBracket)
        )
        val expectedOutputText =
            "$openingBracket\n" +
            "    $openingBracket\n" +
            "        abc;\n" +
            "    $closingBracket\n" +
            closingBracket

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        MatcherAssert.assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun openingAndClosingBracketsInSameLine_doesNotChangeIndent_atLineStart()
    {
        val inputTokens = arrayListOf(
            SpecialToken(openingBracket), UnknownToken("abc"), SpecialToken(";"), SpecialToken(closingBracket), LineBreakToken("\n"),
            UnknownToken("def"), SpecialToken(";")
        )
        val expectedOutputText =
            "${openingBracket}abc;$closingBracket\n" +
            "def;"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        MatcherAssert.assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun openingAndClosingBracketsInSameLine_doesNotChangeIndent_atLineMiddle()
    {
        val inputTokens = arrayListOf(
            UnknownToken("abc"), SpecialToken(openingBracket), UnknownToken("def"), SpecialToken(";"), SpecialToken(closingBracket), LineBreakToken("\n"),
            UnknownToken("ghi"), SpecialToken(";")
        )
        val expectedOutputText =
            "abc${openingBracket}def;$closingBracket\n" +
            "ghi;"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        MatcherAssert.assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun openingAndClosingBracketsInSameLine_doesNotChangeIndent_alreadyIndented()
    {
        val inputTokens = arrayListOf(
            UnknownToken("abc"), SpecialToken(openingBracket), LineBreakToken("\n"),
            SpecialToken(openingBracket), UnknownToken("def"), SpecialToken(";"), SpecialToken(closingBracket), LineBreakToken("\n"),
            UnknownToken("ghi"), SpecialToken(";")
        )
        val expectedOutputText =
            "abc$openingBracket\n" +
            "    ${openingBracket}def;$closingBracket\n" +
            "    ghi;"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        MatcherAssert.assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun openingBracket_atLineEnd()
    {
        val inputTokens = arrayListOf(
            UnknownToken("abc"), SpecialToken(openingBracket), LineBreakToken("\n"),
            UnknownToken("def"), SpecialToken(";"), LineBreakToken("\n"),
            SpecialToken(closingBracket)
        )
        val expectedOutputText =
            "abc$openingBracket\n" +
            "    def;\n" +
            closingBracket

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        MatcherAssert.assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun keywordAndBrackets_atLineStart()
    {
        val inputTokens = arrayListOf(
            KeywordToken("if"), WhiteSpaceToken(" "), SpecialToken("("), UnknownToken("a"), WhiteSpaceToken(" "), SpecialToken("=="), WhiteSpaceToken(" "), UnknownToken("b"), SpecialToken(")"), LineBreakToken("\n"),
            SpecialToken(openingBracket), LineBreakToken("\n"),
            UnknownToken("abc"), SpecialToken(";"), LineBreakToken("\n"),
            SpecialToken(closingBracket)
        )
        val expectedOutputText =
            "if (a == b)\n" +
            "$openingBracket\n" +
            "    abc;\n" +
            closingBracket

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        MatcherAssert.assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun keywordAndOpeningBracketInSameLine()
    {
        val inputTokens = arrayListOf(
            KeywordToken("if"), WhiteSpaceToken(" "), SpecialToken("("), UnknownToken("a"), WhiteSpaceToken(" "), SpecialToken("=="), WhiteSpaceToken(" "), UnknownToken("b"), SpecialToken(")"), WhiteSpaceToken(" "), SpecialToken(openingBracket), LineBreakToken("\n"),
            UnknownToken("abc"), SpecialToken(";"), LineBreakToken("\n"),
            SpecialToken(closingBracket)
        )
        val expectedOutputText =
            "if (a == b) $openingBracket\n" +
            "    abc;\n" +
            closingBracket

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        MatcherAssert.assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
