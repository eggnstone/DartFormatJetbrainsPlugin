package splitters.text

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.*
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TextSplitter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestSingleBlocks
{
    @Test
    fun unexpectedClosingCurlyBracket()
    {
        val inputText = "}"

        assertThrows<DartFormatException> { TextSplitter().split(inputText) }
    }

    @Test
    fun simpleBlock()
    {
        val inputText = "{}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("{", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = TextSplitter().split(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))

        PartTools.printParts(result.parts)
    }

    @Test
    fun simpleBlockWithTextBefore()
    {
        val inputText = "abc {}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("abc {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = TextSplitter().split(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))

        PartTools.printParts(result.parts)
    }

    @Test
    fun conditionalWithSimpleIf()
    {
        val inputText = "if (true) { statement; }"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace(" "), Statement("statement;"), Whitespace(" "))
        val expectedPart = SingleBlock("if (true) {", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        val result = TextSplitter().split(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))

        PartTools.printParts(result.parts)
    }
}
