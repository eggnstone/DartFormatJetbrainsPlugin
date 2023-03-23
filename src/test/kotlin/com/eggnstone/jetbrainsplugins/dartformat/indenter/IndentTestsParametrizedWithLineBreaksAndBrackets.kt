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
class IndentTestsParametrizedWithLineBreaksAndBrackets(private val lineBreak: String, private val openingBracket: String, private val closingBracket: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{3}")
        fun data() = TestParams.lineBreaksAndBrackets
    }

    @Test
    fun indentBrackets()
    {
        val inputTokens = mutableListOf(
            SpecialToken(openingBracket),
            LineBreakToken(lineBreak),
            UnknownToken("Text"),
            LineBreakToken(lineBreak),
            SpecialToken(closingBracket),
            LineBreakToken(lineBreak),
            UnknownToken("END")
        )
        val expectedOutputText = "$openingBracket$lineBreak" +
            "    Text$lineBreak" +
            "$closingBracket$lineBreak" +
            "END"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
