package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestStatementsAndComments
{
    @Test
    fun endOfLineCommentAndStatement()
    {
        val comment = "// for () abc();\n"
        val statement = "def();"

        val inputText = comment + statement

        @Suppress("UnnecessaryVariable")
        val expectedRemainingText = statement
        val expectedPart = Comment(comment)
        val expectedParts = listOf(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun multiLineCommentAndStatement()
    {
        val comment =
            "/*\n" +
                "for ()\n" +
                "  abc();\n" +
                "*/"
        val statement = "def();"

        val inputText = comment + "\n" + statement

        val expectedRemainingText = "\n" + statement
        val expectedPart = Comment(comment)
        val expectedParts = listOf(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun statementAndMultiLineComment()
    {
        val statement = "abc();"
        val comment =
            "/*\n" +
                "for ()\n" +
                "  def();\n" +
                "*/"

        val inputText = "$statement $comment"

        val expectedRemainingText = " $comment"
        val expectedPart = Statement(statement)
        val expectedParts = listOf(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
