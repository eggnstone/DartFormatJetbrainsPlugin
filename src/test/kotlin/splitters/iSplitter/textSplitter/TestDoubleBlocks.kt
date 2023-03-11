package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestDoubleBlocks
{
    @Test
    fun ifBlockAndElseBlock()
    {
        val inputText = "if (true) { statement1; } else { statement2; }"

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val parts2 = listOf(Whitespace(" "), Statement("statement2;"), Whitespace(" "))
        val expectedPart = DoubleBlock("if (true) {", "} else {", "}", parts1, parts2)
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
        val expectedPart = DoubleBlock("if (true) {", "} else{", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifBlockAndElseBlockAndTabAfterElse()
    {
        val inputText = "if (true) { statement1; } else\t{ statement2; }"

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val parts2 = listOf(Whitespace(" "), Statement("statement2;"), Whitespace(" "))
        val expectedPart = DoubleBlock("if (true) {", "} else\t{", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
