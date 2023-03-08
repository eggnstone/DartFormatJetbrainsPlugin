package com.eggnstone.jetbrainsplugins.dartformat.indenter

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.junit.Test

class IndentTests
{
    @Test
    fun whiteSpace_atLineStart()
    {
        val inputTokens = mutableListOf(WhiteSpaceToken(" "), UnknownToken("abc"), SpecialToken(";"))
        val expectedOutputText = "abc;"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        TestTools.assertAreEqual(actualOutputText, expectedOutputText)
    }
}
