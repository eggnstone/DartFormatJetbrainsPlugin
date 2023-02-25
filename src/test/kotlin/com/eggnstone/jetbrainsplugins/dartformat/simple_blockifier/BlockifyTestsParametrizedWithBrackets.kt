package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.blocks.SimpleInstructionBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class BlockifyTestsParametrizedWithBrackets(
    private val openingBracket: String,
    private val closingBracket: String,
    @Suppress("UNUSED_PARAMETER") unused: String
)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{2}")
        fun data() = TestParams.brackets
    }

    @Test
    fun unexpectedEndInBracketBlock()
    {
        val inputText = openingBracket

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }

    @Test
    fun unexpectedClosingBracket()
    {
        val inputText = closingBracket

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }

    @Test
    fun unexpectedClosingBracketAfterText()
    {
        val inputText = "a$closingBracket"

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }

    @Test
    fun oneBracketBlock_okWhenCurlyBrackets()
    {
        val inputText = "$openingBracket$closingBracket"

        if (closingBracket != "}")
            return

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = arrayListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun oneBracketBlock_exceptionWhenNotCurlyBracket()
    {
        val inputText = "$openingBracket$closingBracket"

        if (closingBracket == "}")
            return

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }

    @Test
    fun twoNestedBracketBlocks_okWhenCurlyBracketsAtEnd()
    {
        val inputText = "$openingBracket$openingBracket$closingBracket$closingBracket"

        if (closingBracket != "}")
            return

        val block = SimpleInstructionBlock("$openingBracket$openingBracket$closingBracket$closingBracket")
        val expectedBlocks = arrayListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun twoNestedBracketBlocks_exceptionWhenNotCurlyBracketAtEnd()
    {
        val inputText = "$openingBracket$openingBracket$closingBracket$closingBracket"

        if (closingBracket == "}")
            return

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }
}
