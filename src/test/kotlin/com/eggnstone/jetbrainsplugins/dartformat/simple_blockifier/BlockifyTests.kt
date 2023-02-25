package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import com.eggnstone.jetbrainsplugins.dartformat.blockifier.BlockifierOld
import com.eggnstone.jetbrainsplugins.dartformat.blocks.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class BlockifyTests
{
    @Test
    fun emptyText()
    {
        val inputText = ""
        val expectedBlocks = arrayListOf<ISimpleBlock>()

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun oneSimpleInstruction()
    {
        val inputText = "abc();"

        val expressionBlock = SimpleInstructionBlock("abc();")
        val expectedBlocks = arrayListOf(expressionBlock)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun twoSimpleInstructions()
    {
        val inputText = "abc();def();"

        val block1 = SimpleInstructionBlock("abc();")
        val block2 = SimpleInstructionBlock("def();")
        val expectedBlocks = arrayListOf(block1, block2)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun twoSimpleInstructionsWithWhitespaceInTheMiddle()
    {
        val inputText = "abc();\ndef();"

        val block1 = SimpleInstructionBlock("abc();")
        val block2 = SimpleWhitespaceBlock("\n")
        val block3 = SimpleInstructionBlock("def();")
        val expectedBlocks = arrayListOf(block1, block2, block3)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun twoSimpleInstructionsWithWhitespacesAround()
    {
        val inputText = "\nabc();\ndef();\n"

        val block1 = SimpleWhitespaceBlock("\n")
        val block2 = SimpleInstructionBlock("abc();")
        val block3 = SimpleWhitespaceBlock("\n")
        val block4 = SimpleInstructionBlock("def();")
        val block5 = SimpleWhitespaceBlock("\n")
        val expectedBlocks = arrayListOf(block1, block2, block3, block4, block5)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }
}
