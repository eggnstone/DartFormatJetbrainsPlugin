package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestSingleBlockEnums
{
    @Test
    fun emptyEnum()
    {
        val inputText = "enum E {}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("enum E {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleEnum1()
    {
        val inputText = "enum E { A }"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace(" "), Statement("A "))
        val expectedPart = SingleBlock("enum E {", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleEnum2()
    {
        val inputText = "enum E { A, B }"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace(" "), Statement("A, B "))
        val expectedPart = SingleBlock("enum E {", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun emptyEnumWithLineBreaks()
    {
        val inputText =
            "enum E\n" +
                "{\n" +
                "}"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace("\n"))
        val expectedPart = SingleBlock("enum E\n{", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun emptyEnumWithLineBreaks1()
    {
        val inputText =
            "enum E\n" +
                "{\n" +
                "  A\n" +
                "}"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace("\n  "), Statement("A\n"))
        val expectedPart = SingleBlock("enum E\n{", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun emptyEnumWithLineBreaks2()
    {
        val inputText =
            "enum E\n" +
                "{\n" +
                "  A,\n" +
                "  B\n" +
                "}"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace("\n  "), Statement("A,\n  B\n"))
        val expectedPart = SingleBlock("enum E\n{", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
