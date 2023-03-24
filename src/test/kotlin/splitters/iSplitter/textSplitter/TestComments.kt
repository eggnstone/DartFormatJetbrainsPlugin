package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestComments
{
    /*@Test
    fun endOfLineCommentAndStatement()
    {
        val inputText =
            "// end of line comment\n" +
                "abc();"

        //val expectedRemainingText = "abc();"
        val expectedRemainingText = ""
        //val expectedParts = listOf(EndOfLineComment("// end of line comment\n") )
        val expectedParts = listOf(Statement("// end of line comment\nabc();"))

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }*/

    @Test
    fun statementAndEndOfLineComment()
    {
        val inputText = "abc(); // end of line comment"

        //val expectedRemainingText = "abc();"
        val expectedRemainingText = ""
        //val expectedParts = listOf(EndOfLineComment("// end of line comment\n") )
        val expectedParts = listOf(Statement("abc(); // end of line comment"))

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun statementAndEndOfLineCommentAndLineBreak()
    {
        val inputText = "abc(); // end of line comment\n"

        //val expectedRemainingText = "abc();"
        val expectedRemainingText = ""
        //val expectedParts = listOf(EndOfLineComment("// end of line comment\n") )
        val expectedParts = listOf(Statement("abc(); // end of line comment\n"))

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    /*@Test
    fun endOfLineCommentAndClosingBrace()
    {
        val inputText =
            "// end of line comment\n" +
            "}"

        val expectedRemainingText = "}"
        val expectedParts = listOf(EndOfLineComment("// end of line comment\n"))

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }*/
}
