package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import splitters.SplitterTestTools

class TestStatementsWithIf
{
    @Test
    fun ifStatement()
    {
        val inputText = "if (true) statement;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifAndEmptyElse_Throws()
    {
        val inputText = "if (true) statement1; else"

        assertThrows<DartFormatException> { TextSplitter().split(inputText) }
    }

    @Test
    fun ifAndElse()
    {
        val inputText = "if (true) statement1; else statement2;"

        val expectedRemainingText = ""
        val expectedPart = Statement("if (true) statement1; else statement2;")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifAndElse2()
    {
        val inputText = "if (true)\nstatement1;\nelse\nstatement2;"

        val expectedRemainingText = ""
        val expectedPart = Statement("if (true)\nstatement1;\nelse\nstatement2;")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifAndMissingElse()
    {
        val inputText = "if (true) statement1; elseX"

        val expectedRemainingText = " elseX"
        val expectedPart = Statement("if (true) statement1;")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifAndElseIf()
    {
        val inputText = "if (a) a(); else if (b) b();"

        val expectedRemainingText = ""
        val expectedPart = Statement("if (a) a(); else if (b) b();")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifAndElseIf2()
    {
        val inputText = "if (a)\na();\nelse if (b)\nb();"

        val expectedRemainingText = ""
        val expectedPart = Statement("if (a)\na();\nelse if (b)\nb();")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifAndElseIfAndElse()
    {
        val inputText = "if (a) a(); else if (b) b(); else c();"

        val expectedRemainingText = ""
        val expectedPart = Statement("if (a) a(); else if (b) b(); else c();")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifAndElseIfAndElse2()
    {
        val inputText = "if (a)\na();\nelse if (b)\nb();\nelse\nc();"

        val expectedRemainingText = ""
        val expectedPart = Statement("if (a)\na();\nelse if (b)\nb();\nelse\nc();")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
