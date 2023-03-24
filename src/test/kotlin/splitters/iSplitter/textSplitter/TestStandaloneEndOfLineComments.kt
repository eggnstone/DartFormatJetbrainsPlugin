package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestStandaloneEndOfLineComments
{
    @Test
    fun onlyComment()
    {
        val inputText = "// for () abc();"

        val expectedRemainingText = ""
        val expectedPart = Comment(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun commentAndStatement()
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
}