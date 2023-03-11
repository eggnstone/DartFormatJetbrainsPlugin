package splitters.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import splitters.SplitterTestTools

class TestDoubleBlocks
{
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
        val parts1 = listOf(Statement("statement1;"))
        val parts2 = listOf(Statement("statement2;"))
        val expectedPart = DoubleBlock("if (true) ", " else ", "", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifWithBracketsAndEmptyElse_Throws()
    {
        val inputText = "if (true) { statement1; } else"

        assertThrows<DartFormatException> { TextSplitter().split(inputText) }
    }

    @Test
    fun ifAndNonWhitespaceAfterElse_statementWithRemainingText()
    {
        val inputText = "if (true) statement1; elseX"

        val expectedRemainingText = " elseX"
        val expectedPart = Statement("if (true) statement1;")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifWithBracketsAndNonWhitespaceAfterElse_Throws()
    {
        val inputText = "if (true) { statement1; } elseX"

        assertThrows<DartFormatException> { TextSplitter().split(inputText) }
    }

    @Test
    fun ifAndNoWhitespaceAfterElse()
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
    fun ifAndSpaceAfterElse()
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
    fun ifAndTabAfterElse()
    {
        val inputText = "if (true) { statement1; } else\t{ statement2; }"

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val parts2 = listOf(Whitespace(" "), Statement("statement2;"), Whitespace(" "))
        val expectedPart = DoubleBlock("if (true) {", "} else\t{", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifWithBracketsAndElseWithoutBrackets()
    {
        val inputText = "if (true) { statement1; } else statement2;"

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val parts2 = listOf(Statement("statement2;"))
        val expectedPart = DoubleBlock("if (true) {", "} else ", "", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifWithBracketsAndElseWithoutBracketsWithMoreWhitespace()
    {
        val inputText =
            "if (true) { statement1; }  \n" +
            "  else  \n" +
            "  statement2;"

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val parts2 = listOf(Statement("statement2;"))
        val expectedPart = DoubleBlock("if (true) {", "}  \n  else  \n  ", "", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun ifWithoutBracketsAndElseWithBrackets()
    {
        val inputText = "if (true) statement1; else { statement2; }"

        val expectedRemainingText = ""
        val parts1 = listOf(Statement("statement1;"))
        val parts2 = listOf(Whitespace(" "), Statement("statement2;"), Whitespace(" "))
        val expectedPart = DoubleBlock("if (true) ", " else{", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
