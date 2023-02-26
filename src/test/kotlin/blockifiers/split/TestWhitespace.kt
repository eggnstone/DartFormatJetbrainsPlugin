/*
package blockifiers.blockify

import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.BlockifierOld2
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.BlockTools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.WhitespaceBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestWhitespace
{
    @Test
    fun oneSpace()
    {
        val inputText = " "

        val block = WhitespaceBlock(" ")
        val expectedBlocks = mutableListOf<IBlock>(block)

        val blockifier = BlockifierOld2()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }

    @Test
    fun oneTab()
    {
        val inputText = "\t"

        val block = WhitespaceBlock("\t")
        val expectedBlocks = mutableListOf<IBlock>(block)

        val blockifier = BlockifierOld2()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }

    @Test
    fun oneNewline()
    {
        val inputText = "\n"

        val block = WhitespaceBlock("\n")
        val expectedBlocks = mutableListOf<IBlock>(block)

        val blockifier = BlockifierOld2()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }

    @Test
    fun oneReturn()
    {
        val inputText = "\r"

        val block = WhitespaceBlock("\r")
        val expectedBlocks = mutableListOf<IBlock>(block)

        val blockifier = BlockifierOld2()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }

    @Test
    fun multipleWhitespaces()
    {
        val inputText = " \n\r\t"

        val block = WhitespaceBlock(" \n\r\t")
        val expectedBlocks = mutableListOf<IBlock>(block)

        val blockifier = BlockifierOld2()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(actualBlocks)
    }
}
*/
