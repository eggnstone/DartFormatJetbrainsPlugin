package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.simple_blocks.ISimpleBlock
import com.eggnstone.jetbrainsplugins.dartformat.simple_blocks.SimpleInstructionBlock
import com.eggnstone.jetbrainsplugins.dartformat.simple_blocks.SimpleWhitespaceBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class BlockifyTests
{
    @Test
    fun emptyText()
    {
        val inputText = ""
        val expectedBlocks = mutableListOf<ISimpleBlock>()

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun oneWhitespace()
    {
        val inputText = " "

        val block = SimpleWhitespaceBlock(inputText)
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun twoWhitespaces()
    {
        val inputText = "  "

        val block = SimpleWhitespaceBlock(inputText)
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun unexpectedEndInSimpleInstruction()
    {
        val inputText = "abc()"

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }

    @Test
    fun oneSimpleInstructionSemicolonOnly()
    {
        val inputText = ";"

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun oneSimpleInstruction()
    {
        val inputText = "abc();"

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = mutableListOf(block)

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
        val expectedBlocks = mutableListOf(block1, block2)

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
        val expectedBlocks = mutableListOf(block1, block2, block3)

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
        val expectedBlocks = mutableListOf(block1, block2, block3, block4, block5)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun unexpectedEndInCurlyBracketBlock()
    {
        val inputText = "{"

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }

    @Test
    fun unexpectedEndInRoundBracketBlock()
    {
        val inputText = "("

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }

    @Test
    fun unexpectedEndInSquareBracketBlock()
    {
        val inputText = "["

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }

    @Test
    fun unexpectedClosingCurlyBracket()
    {
        val inputText = "a}"

        assertThrows<DartFormatException> { SimpleBlockifier().blockify(inputText) }
    }

    @Test
    fun oneCurlyBracketBlock()
    {
        val inputText = "{}"

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = mutableListOf(block)

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
        val expectedBlocks = mutableListOf(block1, block2)

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
        val expectedBlocks = mutableListOf(block)

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
        val expectedBlocks = mutableListOf(block1, block2)

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
        val expectedBlocks = mutableListOf(block1, block2)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun simpleInstructionInsideCurlyBracketBlock()
    {
        val inputText = "{abc();}"

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun curlyBracketBlockInsideSimpleInstruction()
    {
        val inputText = "abc({});"

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun conditionalAndSimpleCurlyBracketBlock()
    {
        val inputText = "if (true) {}"

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun conditionalAndSimpleExpression()
    {
        val inputText = "if (true) ;"

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }
}
