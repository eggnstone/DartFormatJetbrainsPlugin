package blockifiers.master.blockify

import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.MasterBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.BlockTools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.InstructionBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.WhitespaceBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{
    @Test
    fun oneWhitespace()
    {
        val inputText = " "

        val block1 = WhitespaceBlock(" ")
        val expectedBlocks = mutableListOf<IBlock>(block1)

        val actualBlocks = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }

    @Test
    fun oneInstruction()
    {
        val inputText = ";"

        val block1 = InstructionBlock(";", "")
        val expectedBlocks = mutableListOf<IBlock>(block1)

        val actualBlocks = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }

    @Test
    fun twoInstructions()
    {
        val inputText = ";;"

        val block1 = InstructionBlock(";", "")
        val block2 = InstructionBlock(";", "")
        val expectedBlocks = mutableListOf<IBlock>(block1, block2)

        val actualBlocks = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }

    @Test
    fun oneWhitespaceAndOneInstruction()
    {
        val inputText = " ;"

        val block1 = WhitespaceBlock(" ")
        val block2 = InstructionBlock(";", "")
        val expectedBlocks = mutableListOf(block1, block2)

        val actualBlocks = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }

    @Test
    fun oneInstructionAndOneWhitespace()
    {
        val inputText = "; "

        val block1 = InstructionBlock(";", "")
        val block2 = WhitespaceBlock(" ")
        val expectedBlocks = mutableListOf(block1, block2)

        val actualBlocks = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }
}
