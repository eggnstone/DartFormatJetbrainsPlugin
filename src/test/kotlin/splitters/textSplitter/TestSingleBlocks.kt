package splitters.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import splitters.SplitterTestTools

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

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleBlockWithTextBefore()
    {
        val inputText = "abc {}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("abc {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun TODO_NAME()
    {
        val inputText =
            "class C\n" +
            "{\n" +
            "}"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace("\n"))
        val expectedPart = SingleBlock("class C\n{", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun pseudoAssignmentWithForLoop()
    {
        val inputText = "for (int i = 0; i < 10; i++) {}"

        val expectedRemainingText = ""
        val parts = listOf<IPart>()
        val expectedPart = SingleBlock("for (int i = 0; i < 10; i++) {", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
