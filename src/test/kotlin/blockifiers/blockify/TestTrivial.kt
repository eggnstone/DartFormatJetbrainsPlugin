package blockifiers.blockify

import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.Blockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.BlockTools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""
        val expectedBlocks = mutableListOf<IBlock>()

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }
}
