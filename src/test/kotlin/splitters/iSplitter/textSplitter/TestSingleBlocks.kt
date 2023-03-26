package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.SplitParams
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import splitters.SplitterTestTools

class TestSingleBlocks
{
    @Test
    fun unexpectedClosingBrace()
    {
        val inputText = "}"
        val inputParams = SplitParams(expectClosingBrace = false)

        assertThrows<DartFormatException> { TextSplitter().split(inputText, inputParams) }
    }

    @Test
    fun expectedClosingBrace()
    {
        val inputText = "}"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = "}"
        val expectedParts = listOf<IPart>()

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }

    @Test
    fun spaceAndExpectedClosingBrace()
    {
        val inputText = " }"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = "}"
        val expectedPart = Statement(" ")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }

    @Test
    fun simpleBlock()
    {
        val inputText = "{}"

        val expectedRemainingText = ""
        val expectedPart = MultiBlock.single("{", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleBlockWithHeader()
    {
        val inputText = "abc {}"

        val expectedRemainingText = ""
        val expectedPart = MultiBlock.single("abc {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun pseudoAssignmentWithForLoop()
    {
        val inputText = "for (int i = 0; i < 10; i++) {}"

        val expectedRemainingText = ""
        val parts = listOf<IPart>()
        val expectedPart = MultiBlock.single("for (int i = 0; i < 10; i++) {", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
