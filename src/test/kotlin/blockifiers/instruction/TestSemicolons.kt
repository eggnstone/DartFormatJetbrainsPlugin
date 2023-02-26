package blockifiers.instruction

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.InstructionBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestSemicolons
{
    @Test
    fun missingSemicolon()
    {
        val inputText = "a"

        assertThrows<DartFormatException> { InstructionBlockifier().blockify(inputText) }
    }

    @Test
    fun singleSemicolon()
    {
        val inputText = ";"

        val expectedRemainingText = ""
        val expectedPart = Statement(";")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = InstructionBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))

        PartTools.printParts(result.parts)
    }

    @Test
    fun simpleFunctionCall()
    {
        val inputText = "abc();"

        val expectedRemainingText = ""
        val expectedPart = Statement("abc();")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = InstructionBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))

        PartTools.printParts(result.parts)
    }
}
