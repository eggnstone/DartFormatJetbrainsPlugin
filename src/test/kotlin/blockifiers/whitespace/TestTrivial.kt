package blockifiers.whitespace

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.WhitespaceBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.BlockTools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.WhitespaceBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun empty()
    {
        val inputText = ""

        assertThrows<DartFormatException> { WhitespaceBlockifier().blockify(inputText) }
    }

    @Test
    fun oneSpace()
    {
        val inputText = " "

        val expectedRemainingText = ""
        val expectedBlock = WhitespaceBlock(" ")
        val expectedBlocks = listOf<IBlock>(expectedBlock)

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(result.blocks)
    }

    @Test
    fun oneTab()
    {
        val inputText = "\t"

        val expectedRemainingText = ""
        val expectedBlock = WhitespaceBlock("\t")
        val expectedBlocks = listOf<IBlock>(expectedBlock)

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(result.blocks)
    }

    @Test
    fun oneNewline()
    {
        val inputText = "\n"

        val expectedRemainingText = ""
        val expectedBlock = WhitespaceBlock("\n")
        val expectedBlocks = listOf<IBlock>(expectedBlock)

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(result.blocks)
    }

    @Test
    fun oneReturn()
    {
        val inputText = "\r"

        val expectedRemainingText = ""
        val expectedBlock = WhitespaceBlock("\r")
        val expectedBlocks = listOf<IBlock>(expectedBlock)

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(result.blocks)
    }

    @Test
    fun multipleWhitespaces()
    {
        val inputText = " \n\r\t"

        val expectedRemainingText = ""
        val expectedBlock = WhitespaceBlock(" \n\r\t")
        val expectedBlocks = listOf<IBlock>(expectedBlock)

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(result.blocks)
    }
}
