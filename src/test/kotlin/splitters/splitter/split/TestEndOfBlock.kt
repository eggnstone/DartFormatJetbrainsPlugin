package splitters.splitter.split

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.Splitter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestEndOfBlock
{
    @Test
    fun closingCurlyBracket()
    {
        val inputText = "}abc();"

        val expectedRemainingText = "}abc();"
        val expectedParts = listOf<IPart>()

        val actualResult = Splitter().split(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }

    @Test
    fun oneInstruction()
    {
        val inputText = ";"

        val expectedRemainingText = ""
        val expectedPart = Statement(";")
        val expectedParts = listOf<IPart>(expectedPart)

        val actualResult = Splitter().split(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }

    @Test
    fun twoInstructions()
    {
        val inputText = ";;"

        val expectedRemainingText = ""
        val expectedPart1 = Statement(";")
        val expectedPart2 = Statement(";")
        val expectedParts = listOf<IPart>(expectedPart1, expectedPart2)

        val actualResult = Splitter().split(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }

    @Test
    fun oneWhitespaceAndOneInstruction()
    {
        val inputText = " ;"

        val expectedRemainingText = ""
        val expectedPart1 = Whitespace(" ")
        val expectedPart2 = Statement(";")
        val expectedParts = listOf(expectedPart1, expectedPart2)

        val actualResult = Splitter().split(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }

    @Test
    fun oneInstructionAndOneWhitespace()
    {
        val inputText = "; "

        val expectedRemainingText = ""
        val expectedPart1 = Statement(";")
        val expectedPart2 = Whitespace(" ")
        val expectedParts = listOf(expectedPart1, expectedPart2)

        val actualResult = Splitter().split(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }
}
