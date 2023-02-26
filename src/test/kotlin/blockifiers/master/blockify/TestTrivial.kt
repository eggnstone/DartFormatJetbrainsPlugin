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
        val expectedRemainingText = ""

        val actualResult = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualResult.blocks)
    }
}
