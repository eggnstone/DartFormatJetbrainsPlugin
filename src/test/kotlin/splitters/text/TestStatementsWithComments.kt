package splitters.text

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestStatementsWithComments
{
    @Test
    fun ifStatement()
    {
        val inputText = "if (true) // falseStatement();\nstatement;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
