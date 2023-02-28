package splitters.whitespace

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.WhitespaceSplitter
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import splitters.SplitterTools

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

        SplitterTools.test(WhitespaceSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
