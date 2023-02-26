package splitters.whitespace

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.WhitespaceSplitter
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

        assertThrows<DartFormatException> { WhitespaceSplitter().split(inputText) }
    }

    @Test
    fun nonWhitespaceEndsBlock()
    {
        val inputText = " a"

        val expectedRemainingText = "a"
        val expectedPart = Whitespace(" ")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = WhitespaceSplitter().split(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))

        PartTools.printParts(result.parts)
    }
}