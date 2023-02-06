package com.eggnstone.jetbrainsplugins.dartformat.indenter

import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class Indent
{
    @Test
    fun indent()
    {
        val inputTokens = arrayListOf(
            EndOfLineCommentToken("end of line comment\n"),
            MultiLineCommentToken("multi line comment"),
            SpecialToken(")"),
            UnknownToken("a"),
            WhiteSpaceToken(" ")
        )
        val expectedOutputText = ""

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        //assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
