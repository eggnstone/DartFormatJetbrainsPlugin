package splitters.block_and_statement

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.BlockAndStatementSplitter
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

        assertThrows<DartFormatException> { BlockAndStatementSplitter().split(inputText) }
    }

    @Test
    fun singleSemicolon()
    {
        val inputText = ";"

        val expectedRemainingText = ""
        val expectedPart = Statement(";")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = BlockAndStatementSplitter().split(inputText)

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

        val result = BlockAndStatementSplitter().split(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))

        PartTools.printParts(result.parts)
    }
}
