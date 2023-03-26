package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestStatementsWithAssignments
{
    @Test
    fun assignmentInt()
    {
        val inputText = "final int i = 0;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun assignmentString()
    {
        val inputText = "final String s = \"abc\";"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun pseudoAssignmentWithForLoop()
    {
        val inputText = "for (int i = 0; i < 10; i++) abc(i);"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun assignmentInConstructorHeader()
    {
        val inputText = "C() : a = b;"

        val expectedRemainingText = ""
        val expectedPart = Statement("C() : a = b;")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun assignmentInConstructorHeaderWithBlock()
    {
        val inputText = "C() : a = b {}"

        val expectedRemainingText = ""
        val expectedPart = MultiBlock.single("C() : a = b {", "}", listOf())
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun assignmentWithBraces()
    {
        val inputText = "final List<String> = <String>{\"a\",\"b\"};"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
