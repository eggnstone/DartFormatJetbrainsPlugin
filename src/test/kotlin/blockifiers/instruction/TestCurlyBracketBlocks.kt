package blockifiers.instruction

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.InstructionBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.BlockInstructionBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.BlockTools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestCurlyBracketBlocks
{
    @Test
    fun unexpectedClosingCurlyBracket()
    {
        val inputText = "}"

        assertThrows<DartFormatException> { InstructionBlockifier().blockify(inputText) }
    }

    @Test
    fun simpleBlock()
    {
        val inputText = "{}"

        val expectedRemainingText = ""
        val expectedBlock = BlockInstructionBlock("{", "}")
        val expectedBlocks = listOf<IBlock>(expectedBlock)

        val result = InstructionBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(result.blocks)
    }
}
