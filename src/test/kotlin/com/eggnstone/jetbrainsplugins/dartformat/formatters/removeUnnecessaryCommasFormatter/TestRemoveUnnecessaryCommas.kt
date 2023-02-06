package com.eggnstone.jetbrainsplugins.dartformat.formatters.removeUnnecessaryCommasFormatter

import com.eggnstone.jetbrainsplugins.dartformat.formatters.RemoveUnnecessaryCommasFormatter
import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TestRemoveUnnecessaryCommas
{
    @Test
    fun removeUnnecessaryComma()
    {
        //val input = ",),},]"
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.CLOSING_ROUND_BRACKET,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_ANGLE_BRACKET,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_SQUARE_BRACKET
        )
        //val expectedOutput = ")}]"
        val expectedOutputTokens = arrayListOf<IToken>(
            SpecialToken.CLOSING_ROUND_BRACKET,
            SpecialToken.CLOSING_ANGLE_BRACKET,
            SpecialToken.CLOSING_SQUARE_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommaWithEndOfLineComment()
    {
        //val input = ",//end of line comment\n)"
        val inputTokens = arrayListOf(
            SpecialToken.COMMA,
            EndOfLineCommentToken("end of line comment\\n"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "//end of line comment\\n)"
        val expectedOutputTokens = arrayListOf(
            EndOfLineCommentToken("end of line comment\\n"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommaWithMultiLineComment()
    {
        //val input = ",/*multi line comment*/)"
        val inputTokens = arrayListOf(
            SpecialToken.COMMA,
            MultiLineCommentToken("multi line comment"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "/*multi line comment*/)"
        val expectedOutputTokens = arrayListOf(
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
        val inputTokens = arrayListOf(
            SpecialToken.COMMA,
            EndOfLineCommentToken("end of line comment\\n"),
            MultiLineCommentToken("multi line comment"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "//end of line comment\\n/*multi line comment*/)"
        val expectedOutputTokens = arrayListOf(
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
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            MultiLineCommentToken("multi line comment"),
            EndOfLineCommentToken("end of line comment\\n"),
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "/*multi line comment*///end of line comment\\n)"
        val expectedOutputTokens = arrayListOf<IToken>(
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
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.COMMA,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = ")"
        val expectedOutputTokens = arrayListOf<IToken>(
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }

    @Test
    fun removeUnnecessaryCommaTwice()
    {
        //val input = ",),)"
        val inputTokens = arrayListOf<IToken>(
            SpecialToken.COMMA,
            SpecialToken.CLOSING_ROUND_BRACKET,
            SpecialToken.COMMA,
            SpecialToken.CLOSING_ROUND_BRACKET
        )
        //val expectedOutput = "))"
        val expectedOutputTokens = arrayListOf<IToken>(
            SpecialToken.CLOSING_ROUND_BRACKET,
            SpecialToken.CLOSING_ROUND_BRACKET
        )

        val actualOutputTokens = RemoveUnnecessaryCommasFormatter().format(inputTokens)

        assertThat(actualOutputTokens, equalTo(expectedOutputTokens))
    }
}
