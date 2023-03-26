package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.SplitParams
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestMultiBlocks
{
    @Test
    fun ifStatementAndElseIfStatementAndElseStatement()
    {
        val inputText = "if (a) a(); else if (b) b(); else c();"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val expectedPart = Statement("if (a) a(); else if (b) b(); else c();")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }

    @Test
    fun ifBlockAndElseBlockWithSpaces()
    {
        val inputText = "if (a) { a(); } else { b(); }"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace(" "), Statement("a();"), Whitespace(" "))
        val parts2 = listOf(Whitespace(" "), Statement("b();"), Whitespace(" "))
        val expectedPart = DoubleBlock("if (a) {", "} else {", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }

    @Test
    fun ifBlockAndElseBlockWithoutSpaces()
    {
        val inputText = "if (a){a();} else {b();}"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val parts1 = listOf(Statement("a();"))
        val parts2 = listOf(Statement("b();"))
        val expectedPart = DoubleBlock("if (a){", "} else {", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }

    @Test
    fun ifBlockAndElseIfBlockAndElseBlockWithoutSpaces()
    {
        val inputText = "if (a){a();} else if (b){b();} else {c();}"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val parts1 = listOf(Statement("a();"))
        val parts2 = listOf(Statement("b();"))
        val expectedPart = DoubleBlock("if (a){", "} else {", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }
}
