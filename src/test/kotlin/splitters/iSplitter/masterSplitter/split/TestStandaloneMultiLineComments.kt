package splitters.iSplitter.masterSplitter.split

import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.MasterSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestStandaloneMultiLineComments
{
    @Test
    fun statementAndComment()
    {
        val statement = "abc();"
        val whitespace = " "
        val comment =
            "/*\n" +
                "for ()\n" +
                "  def();\n" +
                "*/"
        val inputText = statement + whitespace + comment

        val expectedRemainingText = ""
        val statementPart = Statement(statement)
        val whitespacePart = Whitespace(whitespace)
        val commentPart = Comment(comment, 99)
        val expectedParts = listOf(statementPart, whitespacePart, commentPart)

        SplitterTestTools.testSplit(MasterSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}