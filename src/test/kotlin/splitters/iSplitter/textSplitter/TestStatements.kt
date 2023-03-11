package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import splitters.SplitterTestTools

class TestStatements
{
    @Test
    fun missingSemicolon()
    {
        val inputText = "a"

        assertThrows<DartFormatException> { TextSplitter().split(inputText) }
    }

    @Test
    fun singleSemicolon()
    {
        val inputText = ";"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleStatement()
    {
        val inputText = "abc();"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

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
    fun ifAndMissingElse()
    {
        val inputText = "if (true) statement1; elseX"

        val expectedRemainingText = " elseX"
        val expectedPart = Statement("if (true) statement1;")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun subLevelBlock()
    {
        val inputText = "abc({});"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun subLevelBlock2()
    {
        val inputText = "setState((){abc();});"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
