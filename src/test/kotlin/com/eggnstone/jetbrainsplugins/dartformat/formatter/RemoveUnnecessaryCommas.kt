package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RemoveUnnecessaryCommas
{
    @Test
    fun removeUnnecessaryComma()
    {
        //val input = ",),},]"
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.CLOSING_BRACKET,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_ANGLE_BRACKET,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_SQUARE_BRACKET
        )
        val expectedOutput = ")}]"

        val actualOutput = Formatter.format(inputTokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommaWithEndOfLineComment()
    {
        //val input = ",//end of line comment\n)"
        val inputTokens = arrayListOf(
            SpecialToken.COMMA,
            EndOfLineCommentToken("end of line comment\\n"),
            SpecialToken.CLOSING_BRACKET
        )
        val expectedOutput = "//end of line comment\\n)"

        val actualOutput = Formatter.format(inputTokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommaWithMultiLineComment()
    {
        //val input = ",/*multi line comment*/)"
        val inputTokens = arrayListOf(
            SpecialToken.COMMA,
            MultiLineCommentToken("multi line comment"),
            SpecialToken.CLOSING_BRACKET
        )
        val expectedOutput = "/*multi line comment*/)"

        val actualOutput = Formatter.format(inputTokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommaWithEndOfLineAndMultiLineComment()
    {
        //val input = ",//end of line comment\n/*multi line comment*/)"
        val inputTokens = arrayListOf(
            SpecialToken.COMMA,
            EndOfLineCommentToken("end of line comment\\n"),
            MultiLineCommentToken("multi line comment"),
            SpecialToken.CLOSING_BRACKET
        )
        val expectedOutput = "//end of line comment\\n/*multi line comment*/)"

        val actualOutput = Formatter.format(inputTokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommaWithMultiLineAndEndOfLineComment()
    {
        //val input = ",/*multi line comment*///end of line comment\n)"
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            MultiLineCommentToken("multi line comment"),
            EndOfLineCommentToken("end of line comment\\n"),
            SpecialToken.CLOSING_BRACKET
        )
        val expectedOutput = "/*multi line comment*///end of line comment\\n)"

        val actualOutput = Formatter.format(inputTokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommas()
    {
        //val input = ",,,)"
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.COMMA,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_BRACKET
        )
        val expectedOutput = ")"

        val actualOutput = Formatter.format(inputTokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }

    @Test
    fun removeUnnecessaryCommaTwice()
    {
        //val input = ",),)"
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.CLOSING_BRACKET,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_BRACKET
        )
        val expectedOutput = "))"

        val actualOutput = Formatter.format(inputTokens)

        assertThat(actualOutput, equalTo(expectedOutput))
    }
}
