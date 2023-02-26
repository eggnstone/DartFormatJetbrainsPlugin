package blockifiers.instruction

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.InstructionBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.BlockTools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.InstructionBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestSemicolons
{
    @Test
    fun missingSemicolon()
    {
        val inputText = "a"

        assertThrows<DartFormatException> { InstructionBlockifier().blockify(inputText) }
    }

    @Test
    fun singleSemicolon()
    {
        val inputText = ";"

        val expectedRemainingText = ""
        val expectedBlock = InstructionBlock(";", "")
        val expectedBlocks = listOf<IBlock>(expectedBlock)

        val result = InstructionBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(result.blocks)
    }

    @Test
    fun simpleFunctionCall()
    {
        val inputText = "abc();"

        val expectedRemainingText = ""
        val expectedBlock = InstructionBlock("abc();", "")
        val expectedBlocks = listOf<IBlock>(expectedBlock)

        val result = InstructionBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(result.blocks)
    }
}
