package blockifiers.master.blockify

import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.MasterBlockifier
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

        val actualBlocks = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }
}
