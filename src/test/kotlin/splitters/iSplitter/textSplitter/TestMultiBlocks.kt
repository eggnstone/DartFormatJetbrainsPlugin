package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.*
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.SplitParams
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestMultiBlocks
{
    @Test
    fun ifStatementAndElseIfStatementAndElseStatementWithSpaces()
    {
        val inputText = "if (a) a(); else if (b) b(); else c();"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val expectedPart = Statement("if (a) a(); else if (b) b(); else c();")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }

    @Test
    fun ifStatementAndElseIfStatementAndElseStatementWithoutSpaces()
    {
        val inputText = "if(a)a();else if(b)b();else c();"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val expectedPart = Statement("if(a)a();else if(b)b();else c();")
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
        val inputText = "if(a){a();}else{b();}"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val parts1 = listOf(Statement("a();"))
        val parts2 = listOf(Statement("b();"))
        val expectedPart = DoubleBlock("if(a){", "}else{", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }

    @Test
    fun ifBlockAndElseIfBlockAndElseBlockWithSpaces()
    {
        val inputText = "if (a) { a(); } else if (b) { b(); } else { c(); }"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val headers = listOf("if (a) {", "} else if (b) {", "} else {")
        val parts1 = listOf(Whitespace(" "), Statement("a();"), Whitespace(" "))
        val parts2 = listOf(Whitespace(" "), Statement("b();"), Whitespace(" "))
        val parts3 = listOf(Whitespace(" "), Statement("c();"), Whitespace(" "))
        val parts = listOf(parts1, parts2, parts3)
        val footer = "}"
        val expectedPart = MultiBlock(headers, parts, footer)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }

    @Test
    fun ifBlockAndElseIfBlockAndElseBlockWithoutSpaces()
    {
        val inputText = "if(a){a();}else if(b){b();}else{c();}"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val headers = listOf("if(a){", "}else if(b){", "}else{")
        val parts1 = listOf(Statement("a();"))
        val parts2 = listOf(Statement("b();"))
        val parts3 = listOf(Statement("c();"))
        val parts = listOf(parts1, parts2, parts3)
        val footer = "}"
        val expectedPart = MultiBlock(headers, parts, footer)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }
}
