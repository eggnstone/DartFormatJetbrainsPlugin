package blockifiers.whitespace

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.WhitespaceBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.WhitespaceBlock
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestWithNonWhitespace
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

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.block, equalTo(expectedBlock))

        DotlinLogger.log(result.block.toString())
    }
}
