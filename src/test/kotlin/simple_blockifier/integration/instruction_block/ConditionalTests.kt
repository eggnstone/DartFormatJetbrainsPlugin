package simple_blockifier.integration.instruction_block

import dev.eggnstone.plugins.jetbrains.dartformat.simple_blockifier.SimpleBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.simple_blocks.SimpleInstructionBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class ConditionalTests
{
    @Test
    fun conditionalWithInstruction()
    {
        val inputText = "if (true) return;"

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun conditionalWithCurlyBracketBlock()
    {
        val inputText = "if (true) { return; }"

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }
}
