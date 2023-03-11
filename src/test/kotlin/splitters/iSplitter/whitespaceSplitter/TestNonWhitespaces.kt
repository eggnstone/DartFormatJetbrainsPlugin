package splitters.iSplitter.whitespaceSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.WhitespaceSplitter
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import splitters.SplitterTestTools

class TestNonWhitespaces
{
    @Test
    fun unexpectedNonWhitespace()
    {
        val inputText = "a"

        assertThrows<DartFormatException> { WhitespaceSplitter().split(inputText) }
    }

    @Test
    fun nonWhitespaceEndsBlock()
    {
        val inputText = " a"

        val expectedRemainingText = "a"
        val expectedPart = Whitespace(" ")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(WhitespaceSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
