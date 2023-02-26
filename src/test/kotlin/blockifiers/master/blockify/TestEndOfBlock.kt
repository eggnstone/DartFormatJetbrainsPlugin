package blockifiers.master.blockify

import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.MasterBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.BlockTools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.PlainInstructionBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.WhitespaceBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestEndOfBlock
{
    @Test
    fun closingCurlyBracket()
    {
        val inputText = "}abc();"

        val expectedRemainingText = "}abc();"
        val expectedBlocks = mutableListOf<IBlock>()

        val actualResult = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualResult.blocks)
    }

    @Test
    fun oneInstruction()
    {
        val inputText = ";"

        val expectedRemainingText = ""
        val block1 = PlainInstructionBlock(";")
        val expectedBlocks = mutableListOf<IBlock>(block1)

        val actualResult = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualResult.blocks)
    }

    @Test
    fun twoInstructions()
    {
        val inputText = ";;"

        val expectedRemainingText = ""
        val block1 = PlainInstructionBlock(";")
        val block2 = PlainInstructionBlock(";")
        val expectedBlocks = mutableListOf<IBlock>(block1, block2)

        val actualResult = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualResult.blocks)
    }

    @Test
    fun oneWhitespaceAndOneInstruction()
    {
        val inputText = " ;"

        val expectedRemainingText = ""
        val block1 = WhitespaceBlock(" ")
        val block2 = PlainInstructionBlock(";")
        val expectedBlocks = mutableListOf(block1, block2)

        val actualResult = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualResult.blocks)
    }

    @Test
    fun oneInstructionAndOneWhitespace()
    {
        val inputText = "; "

        val expectedRemainingText = ""
        val block1 = PlainInstructionBlock(";")
        val block2 = WhitespaceBlock(" ")
        val expectedBlocks = mutableListOf(block1, block2)

        val actualResult = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualResult.blocks)
    }
}
