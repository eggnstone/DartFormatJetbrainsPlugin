package splitters.master.split

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.MasterSplitter
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

        val actualResult = MasterSplitter().split(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }

    @Test
    fun oneStatement()
    {
        val inputText = ";"

        val expectedRemainingText = ""
        val expectedPart = Statement(";")
        val expectedParts = listOf<IPart>(expectedPart)

        val actualResult = MasterSplitter().split(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }

    @Test
    fun twoStatements()
    {
        val inputText = ";;"

        val expectedRemainingText = ""
        val expectedPart1 = Statement(";")
        val expectedPart2 = Statement(";")
        val expectedParts = listOf<IPart>(expectedPart1, expectedPart2)

        val actualResult = MasterSplitter().split(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }

    @Test
    fun oneWhitespaceAndOneStatement()
    {
        val inputText = " ;"

        val expectedRemainingText = ""
        val expectedPart1 = Whitespace(" ")
        val expectedPart2 = Statement(";")
        val expectedParts = listOf(expectedPart1, expectedPart2)

        val actualResult = MasterSplitter().split(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }

    @Test
    fun oneStatementAndOneWhitespace()
    {
        val inputText = "; "

        val expectedRemainingText = ""
        val expectedPart1 = Statement(";")
        val expectedPart2 = Whitespace(" ")
        val expectedParts = listOf(expectedPart1, expectedPart2)

        val actualResult = MasterSplitter().split(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }
}
