package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.*
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
        val expectedPart = MultiBlock.single("class C {", "}")
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
        val expectedPart = MultiBlock.single("class C\n{", "}", parts)
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
        val parts = listOf(
            Whitespace("\n"),
            Comment("// COMMENT\n")
        )
        val expectedPart = MultiBlock.single("class C\n{", "}", parts)
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
        val parts = listOf(
            Whitespace("\n"),
            Comment("// COMMENT1\n"),
            Statement("abc();\n"),
            Comment("// COMMENT2\n")
        )
        val expectedPart = MultiBlock.single("class C\n{", "}", parts)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleAbstractClass()
    {
        val inputText = "abstract class C {}"

        val expectedRemainingText = ""
        val expectedPart = MultiBlock.single("abstract class C {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
