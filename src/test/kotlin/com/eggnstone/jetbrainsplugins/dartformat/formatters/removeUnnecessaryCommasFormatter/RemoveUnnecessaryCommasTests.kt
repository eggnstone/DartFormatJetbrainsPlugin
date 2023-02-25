package com.eggnstone.jetbrainsplugins.dartformat.formatters.removeUnnecessaryCommasFormatter

import com.eggnstone.jetbrainsplugins.dartformat.formatters.RemoveUnnecessaryCommasFormatter
import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RemoveUnnecessaryCommasTests
{
    @Test
    fun removeUnnecessaryComma()
    {
        //val input = ",),},]"
        val inputTokens = mutableListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.CLOSING_ROUND_BRACKET,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_CURLY_BRACKET,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_SQUARE_BRACKET
        )
        //val expectedOutput = ")}]"
        val expectedOutputTokens = mutableListOf<IToken>(
            SpecialToken.CLOSING_ROUND_BRACKET,
            SpecialToken.CLOSING_CURLY_BRACKET,
            SpecialToken.CLOSING_SQUARE_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommaWithMultiLineComment()
    {
        //val input = ",/*multi line comment*/)"
        val inputTokens = mutableListOf(
            SpecialToken.COMMA,
            MultiLineCommentToken("multi line comment"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "/*multi line comment*/)"
        val expectedOutputTokens = mutableListOf(
            MultiLineCommentToken("multi line comment"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommaWithEndOfLineAndMultiLineComment()
    {
        //val input = ",//end of line comment\n/*multi line comment*/)"
        val inputTokens = mutableListOf(
            SpecialToken.COMMA,
            EndOfLineCommentToken("end of line comment\\n"),
            MultiLineCommentToken("multi line comment"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "//end of line comment\\n/*multi line comment*/)"
        val expectedOutputTokens = mutableListOf(
            EndOfLineCommentToken("end of line comment\\n"),
            MultiLineCommentToken("multi line comment"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommaWithMultiLineAndEndOfLineComment()
    {
        //val input = ",/*multi line comment*///end of line comment\n)"
        val inputTokens = mutableListOf(
            SpecialToken.COMMA,
            MultiLineCommentToken("multi line comment"),
            EndOfLineCommentToken("end of line comment\\n"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "/*multi line comment*///end of line comment\\n)"
        val expectedOutputTokens = mutableListOf(
            MultiLineCommentToken("multi line comment"),
            EndOfLineCommentToken("end of line comment\\n"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommas()
    {
        //val input = ",,,)"
        val inputTokens = mutableListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.COMMA,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = ")"
        val expectedOutputTokens = mutableListOf<IToken>(
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommaTwice()
    {
        //val input = ",),)"
        val inputTokens = mutableListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.CLOSING_ROUND_BRACKET,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "))"
        val expectedOutputTokens = mutableListOf<IToken>(
            SpecialToken.CLOSING_ROUND_BRACKET,
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryComma222222222()
    {
        //val input = ",$newLine)"
        val inputTokens = mutableListOf(
            SpecialToken.COMMA,
            LineBreakToken("\n"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "$newLine)"
        val expectedOutputTokens = mutableListOf(
            LineBreakToken("\n"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommaWithEndOfLineComment()
    {
        //val input = ",//end of line comment\n)"
        val inputTokens = mutableListOf(
            SpecialToken.COMMA,
            EndOfLineCommentToken("end of line comment"),
            LineBreakToken("\n"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "//end of line comment\\n)"
        val expectedOutputTokens = mutableListOf(
            EndOfLineCommentToken("end of line comment"),
            LineBreakToken("\n"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommasWithSpace()
    {
        //val input = ",$newLine )"
        val inputTokens = mutableListOf(
            SpecialToken.COMMA,
            LineBreakToken("\n"),
            WhiteSpaceToken(" "),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "$newLine )"
        val expectedOutputTokens = mutableListOf(
            LineBreakToken("\n"),
            WhiteSpaceToken(" "),
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }
}
