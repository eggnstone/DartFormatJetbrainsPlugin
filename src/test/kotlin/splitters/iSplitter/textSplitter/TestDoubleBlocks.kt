package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.SplitParams
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestDoubleBlocks
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
        val inputText = "if (a){a();}else{b();}"
        val inputParams = SplitParams(expectClosingBrace = true)

        val expectedRemainingText = ""
        val parts1 = listOf(Statement("a();"))
        val parts2 = listOf(Statement("b();"))
        val expectedPart = MultiBlock.double("if (a){", "}else{", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts, inputParams)
    }

    @Test
    fun ifBlockAndElseBlockWithLineBreaks()
    {
        val inputText = "if (true)\n" +
        "{\n" +
        "  statement1;\n" +
        "}\n" +
        "else\n" +
        "{\n" +
        "  statement2;\n" +
        "}"

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace("\n  "), Statement("statement1;"), Whitespace("\n"))
        val parts2 = listOf(Whitespace("\n  "), Statement("statement2;"), Whitespace("\n"))
        val expectedPart = MultiBlock.double("if (true)\n{", "}\nelse\n{", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifBlockAndElseBlockWithLineBreaksAlsoAtTheEnd()
    {
        val inputText = "if (true)\n" +
        "{\n" +
        "  statement1;\n" +
        "}\n" +
        "else\n" +
        "{\n" +
        "  statement2;\n" +
        "}\n"

        val expectedRemainingText = "\n"
        val parts1 = listOf(Whitespace("\n  "), Statement("statement1;"), Whitespace("\n"))
        val parts2 = listOf(Whitespace("\n  "), Statement("statement2;"), Whitespace("\n"))
        val expectedPart = MultiBlock.double("if (true)\n{", "}\nelse\n{", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifBlockAndElseBlockAndNoWhitespaceAfterElse()
    {
        val inputText = "if (true) { statement1; } else{ statement2; }"

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val parts2 = listOf(Whitespace(" "), Statement("statement2;"), Whitespace(" "))
        val expectedPart = MultiBlock.double("if (true) {", "} else{", "}", parts1, parts2)
        val expectedParts = listOf(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifBlockAndElseBlockAndTabAfterElse()
    {
        val inputText = "if (true) { statement1; } else\t{ statement2; }"

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val parts2 = listOf(Whitespace(" "), Statement("statement2;"), Whitespace(" "))
        val expectedPart = MultiBlock.double("if (true) {", "} else\t{", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
