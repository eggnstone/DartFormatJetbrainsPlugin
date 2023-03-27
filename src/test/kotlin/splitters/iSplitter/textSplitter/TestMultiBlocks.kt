package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.SplitParams
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestMultiBlocks
{
    @Test
    fun ifBlockAndElseBlockWithSpaces()
    {
        val inputText = "if (a) { a(); } else { b(); }"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace(" "), Statement("a();"), Whitespace(" "))
        val parts2 = listOf(Whitespace(" "), Statement("b();"), Whitespace(" "))
        val expectedPart = MultiBlock.double("if (a) {", "} else {", "}", parts1, parts2)
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
        val expectedPart = MultiBlock.double("if(a){", "}else{", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }

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
    fun ifBlockAndElseIfBlockAndElseBlockWithSpaces()
    {
        val inputText = "if (a) { a(); } else if (b) { b(); } else { c(); }"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val expectedHeaders = listOf("if (a) {", "} else if (b) {", "} else {")
        val expectedParts1 = listOf(Whitespace(" "), Statement("a();"), Whitespace(" "))
        val expectedParts2 = listOf(Whitespace(" "), Statement("b();"), Whitespace(" "))
        val expectedParts3 = listOf(Whitespace(" "), Statement("c();"), Whitespace(" "))
        val expectedParts = listOf(expectedParts1, expectedParts2, expectedParts3)
        val expectedFooter = "}"
        val expectedPart22 = MultiBlock(expectedHeaders, expectedParts, expectedFooter)
        val expectedParts22 = listOf<IPart>(expectedPart22)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts22, inputParams)
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
        val expectedParts22 = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts22, inputParams)
    }

    @Test
    fun ifBlockAndElseIfBlockAndElseStatementWithSpaces()
    {
        val inputText = "if (a) { a(); } else if (b) { b(); } else c();"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val expectedHeaders = listOf("if (a) {", "} else if (b) {")
        val expectedParts1 = listOf(Whitespace(" "), Statement("a();"), Whitespace(" "))
        val expectedParts2 = listOf(Whitespace(" "), Statement("b();"), Whitespace(" "))
        val expectedParts = listOf(expectedParts1, expectedParts2)
        val expectedFooter = "} else c();"
        val expectedPart22 = MultiBlock(expectedHeaders, expectedParts, expectedFooter)
        val expectedParts22 = listOf<IPart>(expectedPart22)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts22, inputParams)
    }

    @Test
    fun ifBlockAndElseIfBlockAndElseStatementWithoutSpaces()
    {
        val inputText = "if(a){a();}else if(b){b();}else c();"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val headers = listOf("if(a){", "}else if(b){")
        val parts1 = listOf(Statement("a();"))
        val parts2 = listOf(Statement("b();"))
        val parts = listOf(parts1, parts2)
        val footer = "}else c();"
        val expectedPart22 = MultiBlock(headers, parts, footer)
        val expectedParts22 = listOf<IPart>(expectedPart22)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts22, inputParams)
    }
}
