package blockifiers.whitespace

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.WhitespaceBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.WhitespaceBlock
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
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

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.block, equalTo(expectedBlock))

        DotlinLogger.log(result.block.toString())
    }

    @Test
    fun oneTab()
    {
        val inputText = "\t"

        val expectedRemainingText = ""
        val expectedBlock = WhitespaceBlock("\t")

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.block, equalTo(expectedBlock))

        DotlinLogger.log(result.block.toString())
    }

    @Test
    fun oneNewline()
    {
        val inputText = "\n"

        val expectedRemainingText = ""
        val expectedBlock = WhitespaceBlock("\n")

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.block, equalTo(expectedBlock))

        DotlinLogger.log(result.block.toString())
    }

    @Test
    fun oneReturn()
    {
        val inputText = "\r"

        val expectedRemainingText = ""
        val expectedBlock = WhitespaceBlock("\r")

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.block, equalTo(expectedBlock))

        DotlinLogger.log(result.block.toString())
    }

    @Test
    fun multipleWhitespaces()
    {
        val inputText = " \n\r\t"

        val expectedRemainingText = ""
        val expectedBlock = WhitespaceBlock(" \n\r\t")

        val result = WhitespaceBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.block, equalTo(expectedBlock))

        DotlinLogger.log(result.block.toString())
    }
}
