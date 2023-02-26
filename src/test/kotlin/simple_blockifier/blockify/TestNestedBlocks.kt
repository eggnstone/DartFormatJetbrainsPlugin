package simple_blockifier.blockify

import dev.eggnstone.plugins.jetbrains.dartformat.simple_blockifier.SimpleBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.simple_blocks.SimpleInstructionBlock2
import dev.eggnstone.plugins.jetbrains.dartformat.simple_blocks.TextPart
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Ignore
import org.junit.Test

class TestNestedBlocks
{
    @Test
    @Ignore
    fun twoNestedBlocks()
    {
        val inputText = "{{}}"

        val part1 = TextPart("{}")
        val block = SimpleInstructionBlock2("{", "}", mutableListOf(part1))
        val expectedBlocks = mutableListOf(block)

        val blockifier = SimpleBlockifier()
        val actualBlocks = blockifier.blockify(inputText, true)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }
}