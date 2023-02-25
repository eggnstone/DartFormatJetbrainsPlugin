package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import com.eggnstone.jetbrainsplugins.dartformat.blocks.ISimpleBlock
import com.eggnstone.jetbrainsplugins.dartformat.blocks.SimpleInstructionBlock
import com.eggnstone.jetbrainsplugins.dartformat.blocks.SimpleWhitespaceBlock
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

        val block = SimpleInstructionBlock("abc();")
        val expectedBlocks = arrayListOf(block)

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

    @Test
    fun oneCurlyBracketBlock()
    {
        val inputText = "{}"

        val block = SimpleInstructionBlock("{}")
        val expectedBlocks = arrayListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun twoCurlyBracketBlocks()
    {
        val inputText = "{}{}"

        val block1 = SimpleInstructionBlock("{}")
        val block2 = SimpleInstructionBlock("{}")
        val expectedBlocks = arrayListOf(block1, block2)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun twoNestedCurlyBracketBlocks()
    {
        val inputText = "{{}}"

        val block = SimpleInstructionBlock("{{}}")
        val expectedBlocks = arrayListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun oneSimpleInstructionAndOneCurlyBracketBlock()
    {
        val inputText = "abc();{}"

        val block1 = SimpleInstructionBlock("abc();")
        val block2 = SimpleInstructionBlock("{}")
        val expectedBlocks = arrayListOf(block1, block2)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun oneCurlyBracketBlockAndOneSimpleInstruction()
    {
        val inputText = "{}abc();"

        val block1 = SimpleInstructionBlock("{}")
        val block2 = SimpleInstructionBlock("abc();")
        val expectedBlocks = arrayListOf(block1, block2)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }
}
