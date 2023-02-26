/*
package blockifiers.blockify

import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.BlockifierOld2
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.BlockTools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.InstructionBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestCurlyBrackets
{
    @Test
    fun simpleBlock()
    {
        val inputText = "{}"

        val block = InstructionBlock("{", "}", listOf<IBlock>())
        val expectedBlocks = mutableListOf<IBlock>(block)

        val blockifier = BlockifierOld2()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }
}
*/
