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

class TestNonWhitespaces
{
    @Test
    fun unexpectedNonWhitespace()
    {
        val inputText = "a"

        assertThrows<DartFormatException> { WhitespaceBlockifier().blockify(inputText) }
    }

    @Test
    fun nonWhitespaceEndsBlock()
    {
        val inputText = " a"

        val expectedRemainingText = "a"
        val expectedBlock = WhitespaceBlock(" ")
        val expectedBlocks = listOf<IBlock>(expectedBlock)

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.blocks, equalTo(expectedBlocks))

        BlockTools.printBlocks(result.blocks)
    }
}
