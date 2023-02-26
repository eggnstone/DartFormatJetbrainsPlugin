package dev.eggnstone.plugins.jetbrains.dartformat.simple_blockifier.integration.instruction_block

import dev.eggnstone.plugins.jetbrains.dartformat.simple_blockifier.SimpleBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.simple_blocks.SimpleInstructionBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class IntegrationTests
{
    @Test
    fun simpleSetState()
    {
        val inputText = "setState(() {});"

        val block = SimpleInstructionBlock(inputText)
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }
}
