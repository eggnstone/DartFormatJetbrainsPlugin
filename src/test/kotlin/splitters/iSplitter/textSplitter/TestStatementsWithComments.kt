package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestStatementsWithComments
{
    @Test
    fun ifStatementWithEndOfLineComment()
    {
        val inputText =
            "if (true) // falseStatement();\n" +
                "statement;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifStatementWithMultiLineComment()
    {
        val inputText =
            "if (true) /* falseStatement(); */\n" +
                "statement;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
