package splitters.iSplitter.whitespaceSplitter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.WhitespaceSplitter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        assertThrows<DartFormatException> { WhitespaceSplitter().split(inputText) }
    }

    @Test
    fun oneSpace()
    {
        val inputText = " "

        val expectedRemainingText = ""
        val expectedPart = Whitespace(" ")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = WhitespaceSplitter().split(inputText)

        TestTools.assertAreEqual(result.remainingText, expectedRemainingText)
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))
    }

    @Test
    fun oneTab()
    {
        val inputText = "\t"

        val expectedRemainingText = ""
        val expectedPart = Whitespace("\t")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = WhitespaceSplitter().split(inputText)

        TestTools.assertAreEqual(result.remainingText, expectedRemainingText)
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))
    }

    @Test
    fun oneNewline()
    {
        val inputText = "\n"

        val expectedRemainingText = ""
        val expectedPart = Whitespace("\n")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = WhitespaceSplitter().split(inputText)

        TestTools.assertAreEqual(result.remainingText, expectedRemainingText)
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))
    }

    @Test
    fun oneReturn()
    {
        val inputText = "\r"

        val expectedRemainingText = ""
        val expectedPart = Whitespace("\r")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = WhitespaceSplitter().split(inputText)

        TestTools.assertAreEqual(result.remainingText, expectedRemainingText)
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))
    }

    @Test
    fun multipleWhitespaces()
    {
        val inputText = " \n\r\t"

        val expectedRemainingText = ""
        val expectedPart = Whitespace(" \n\r\t")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = WhitespaceSplitter().split(inputText)

        TestTools.assertAreEqual(result.remainingText, expectedRemainingText)
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))
    }
}
