package blockifiers.master.blockify

import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.MasterBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
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
        val expectedParts = mutableListOf<IPart>()

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
        val block1 = Statement(";")
        val expectedParts = mutableListOf<IPart>(block1)

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
        val block1 = Statement(";")
        val block2 = Statement(";")
        val expectedParts = mutableListOf<IPart>(block1, block2)

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
        val block1 = Whitespace(" ")
        val block2 = Statement(";")
        val expectedParts = mutableListOf(block1, block2)

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
        val block1 = Statement(";")
        val block2 = Whitespace(" ")
        val expectedParts = mutableListOf(block1, block2)

        val actualResult = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }
}
