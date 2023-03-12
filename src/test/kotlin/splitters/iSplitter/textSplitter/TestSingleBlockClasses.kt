package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestSingleBlockClasses
{
    @Test
    fun simpleClass()
    {
        val inputText = "class C {}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("class C {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleClassWithLineBreaks()
    {
        val inputText =
            "class C\n" +
            "{\n" +
            "}"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace("\n"))
        val expectedPart = SingleBlock("class C\n{", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleClassWithLineBreaksAndComment()
    {
        val inputText =
            "class C\n" +
            "{\n" +
            "// COMMENT\n" +
            "}"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace("\n"), Statement("// COMMENT\n"))
        val expectedPart = SingleBlock("class C\n{", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleClassWithLineBreaksAndCommentAndStatementAndComment()
    {
        val inputText =
            "class C\n" +
            "{\n" +
            "// COMMENT1\n" +
            "abc();\n" +
            "// COMMENT2\n" +
            "}"

        val expectedRemainingText = ""
        val parts = listOf(Whitespace("\n"), Statement("// COMMENT1\nabc();\n"), Statement("// COMMENT2\n"))
        val expectedPart = SingleBlock("class C\n{", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleAbstractClass()
    {
        val inputText = "abstract class C {}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("abstract class C {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
