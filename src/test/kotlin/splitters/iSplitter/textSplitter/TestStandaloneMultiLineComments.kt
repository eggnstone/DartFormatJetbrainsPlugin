package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestStandaloneMultiLineComments
{
    @Test
    fun onlyComment()
    {
        val inputText =
            "/*\n" +
                "for ()\n" +
                "  abc();\n" +
                "*/"

        val expectedRemainingText = ""
        val expectedPart = Comment(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun commentAndStatement()
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
}