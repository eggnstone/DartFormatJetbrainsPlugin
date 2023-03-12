package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
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
    fun ifBlock()
    {
        val inputText = "if (true) { statement; }"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace(" "), Statement("statement;"), Whitespace(" "))
        val expectedPart = SingleBlock("if (true) {", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifBlockAndElseStatement()
    {
        val inputText = "if (true) { statement1; } else statement2;"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val expectedPart = SingleBlock("if (true) {", "} else statement2;", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifBlockAndElseStatementWithMoreWhitespace()
    {
        val inputText =
            "if (true) { statement1; }  \n" +
            "  else  \n" +
            "  statement2;"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val expectedPart = SingleBlock("if (true) {", "}  \n  else  \n  statement2;", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifStatementAndElseBlock()
    {
        val inputText = "if (true) statement1; else { statement2; }"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace(" "), Statement("statement2;"), Whitespace(" "))
        val expectedPart = SingleBlock("if (true) statement1; else {", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifBlockAndEmptyElse_Throws()
    {
        val inputText = "if (true) { statement1; } else"

        assertThrows<DartFormatException> { TextSplitter().split(inputText) }
    }

    @Test
    fun ifBlockAndMissingElse()
    {
        val inputText = "if (true) { statement1; } elseX"

        val expectedRemainingText = " elseX"
        val parts = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val expectedPart = SingleBlock("if (true) {", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
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
    fun simpleBlockWithHeader()
    {
        val inputText = "abc {}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("abc {", "}")
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
