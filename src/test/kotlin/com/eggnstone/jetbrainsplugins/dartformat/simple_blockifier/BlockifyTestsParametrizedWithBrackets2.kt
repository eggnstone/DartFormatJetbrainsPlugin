package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.simple_blocks.SimpleInstructionBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class BlockifyTestsParametrizedWithBrackets2(
    private val openingBracket1: String,
    private val closingBracket1: String,
    private val openingBracket2: String,
    private val closingBracket2: String,
    @Suppress("UNUSED_PARAMETER") unused: String
)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{4}")
        fun data() = TestParams.brackets2
    }

    @Test
    fun twoConsecutiveBracketBlocks_okWhenLastIsCurly()
    {
        val inputText = "$openingBracket1$closingBracket1$openingBracket2$closingBracket2"

        if (closingBracket2 != "}")
            return

        // handled in test "twoCurlyBracketBlocks"
        if (openingBracket1 == "{")
            return

        val block = SimpleInstructionBlock("$openingBracket1$closingBracket1$openingBracket2$closingBracket2")
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun twoConsecutiveBracketBlocks_exceptionWhenLastIsNotCurly()
    {
        val inputText = "$openingBracket1$closingBracket1$openingBracket2$closingBracket2"

        if (closingBracket2 == "}")
            return

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }

    @Test
    fun twoNestedBracketBlocks_okWhenOuterIsCurly()
    {
        val inputText = "$openingBracket1$openingBracket2$closingBracket2$closingBracket1"

        if (openingBracket1 != "{")
            return

        // handled in test "twoNestedCurlyBracketBlocks"
        if (openingBracket2 == "{")
            return

        val block = SimpleInstructionBlock("$openingBracket1$openingBracket2$closingBracket2$closingBracket1")
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun twoNestedBracketBlocks_exceptionWhenOuterIsNotCurly()
    {
        val inputText = "$openingBracket1$openingBracket2$closingBracket2$closingBracket1"

        if (openingBracket1 == "{")
            return

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }
}
