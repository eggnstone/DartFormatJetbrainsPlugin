package blockifiers.master.blockify

import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.MasterBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{
    @Test
    fun oneWhitespace()
    {
        val inputText = " "

        val expectedRemainingText = ""
        val expectedPart = Whitespace(" ")
        val expectedParts = listOf<IPart>(expectedPart)

        val actualResult = MasterBlockifier().blockify(inputText)

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
        val expectedParts = mutableListOf<IPart>(expectedPart)

        val actualResult = MasterBlockifier().blockify(inputText)

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
        val expectedParts = mutableListOf<IPart>(expectedPart1, expectedPart2)

        val actualResult = MasterBlockifier().blockify(inputText)

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
        val expectedParts = mutableListOf(expectedPart1, expectedPart2)

        val actualResult = MasterBlockifier().blockify(inputText)

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
        val expectedParts = mutableListOf(expectedPart1, expectedPart2)

        val actualResult = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }
}
