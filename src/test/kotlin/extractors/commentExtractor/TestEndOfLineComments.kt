package extractors.commentExtractor

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import org.junit.Test

class TestEndOfLineComments
{
    @Test
    fun endOfLineCommentWithoutLineBreak()
    {
        val inputText = "// end of line comment"

        val expectedComment = "// end of line comment"
        val expectedRemainingText = ""

        val actualResult = CommentExtractor.extract(inputText)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }

    @Test
    fun endOfLineCommentWithLineBreak()
    {
        val inputText =
            "// end of line comment\n" +
            "abc();"

        val expectedComment = "// end of line comment\n"
        val expectedRemainingText = "abc();"

        val actualResult = CommentExtractor.extract(inputText)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }
}
