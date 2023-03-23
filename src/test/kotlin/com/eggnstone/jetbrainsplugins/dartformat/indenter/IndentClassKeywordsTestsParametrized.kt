package com.eggnstone.jetbrainsplugins.dartformat.indenter

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class IndentClassKeywordsTestsParametrized(private val mainClassKeyword: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = arrayOf("class", "abstract class")
    }

    @Test
    fun mainClassKeyword_singleLine()
    {
        val inputTokens = mutableListOf(
            ClassKeywordToken(mainClassKeyword), WhiteSpaceToken(" "), UnknownToken("C"), WhiteSpaceToken(" "), SpecialToken.OPENING_CURLY_BRACKET, SpecialToken.CLOSING_CURLY_BRACKET, LineBreakToken("\n"),
            UnknownToken("abc;")
        )
        val expectedOutputText =
            "$mainClassKeyword C {}\n" +
                "abc;"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        TestTools.assertAreEqual("Indented text", actualOutputText, expectedOutputText)
    }

    @Test
    fun mainClassKeyword_twoLines()
    {
        val inputTokens = mutableListOf(
            ClassKeywordToken(mainClassKeyword), WhiteSpaceToken(" "), UnknownToken("C"), LineBreakToken("\n"),
            SpecialToken.OPENING_CURLY_BRACKET
        )
        val expectedOutputText =
            "$mainClassKeyword C\n" +
                "{"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        TestTools.assertAreEqual("Indented text", actualOutputText, expectedOutputText)
    }

    @Test
    fun mainClassKeyword_TODO()
    {
        val inputTokens = mutableListOf(
            ClassKeywordToken(mainClassKeyword), WhiteSpaceToken(" "), UnknownToken("C"), LineBreakToken("\n"),
            ClassKeywordToken("with"), WhiteSpaceToken(" "), UnknownToken("_\$C"), LineBreakToken("\n"),
            SpecialToken.OPENING_CURLY_BRACKET
        )
        val expectedOutputText =
            "$mainClassKeyword C\n" +
                "    with _\$C\n" +
                "{"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        TestTools.assertAreEqual("Indented text", actualOutputText, expectedOutputText)
    }

    @Test
    fun mainClassKeyword_threeLines()
    {
        val inputTokens = mutableListOf(
            ClassKeywordToken(mainClassKeyword), WhiteSpaceToken(" "), UnknownToken("C"), LineBreakToken("\n"),
            ClassKeywordToken("CK2"), WhiteSpaceToken(" "), UnknownToken("C2"), LineBreakToken("\n"),
            SpecialToken.OPENING_CURLY_BRACKET
        )
        val expectedOutputText =
            "$mainClassKeyword C\n" +
                "    CK2 C2\n" +
                "{"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        TestTools.assertAreEqual("Indented text", actualOutputText, expectedOutputText)
    }

    @Test
    fun mainClassKeyword_fourLines()
    {
        val inputTokens = mutableListOf(
            ClassKeywordToken(mainClassKeyword), WhiteSpaceToken(" "), UnknownToken("C"), LineBreakToken("\n"),
            ClassKeywordToken("CK2"), WhiteSpaceToken(" "), UnknownToken("C2"), LineBreakToken("\n"),
            ClassKeywordToken("CK3"), WhiteSpaceToken(" "), UnknownToken("C3"), LineBreakToken("\n"),
            SpecialToken.OPENING_CURLY_BRACKET
        )
        val expectedOutputText =
            "$mainClassKeyword C\n" +
                "    CK2 C2\n" +
                "    CK3 C3\n" +
                "{"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        TestTools.assertAreEqual("Indented text", actualOutputText, expectedOutputText)
    }
}
