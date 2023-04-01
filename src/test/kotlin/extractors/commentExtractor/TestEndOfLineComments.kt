package extractors.commentExtractor

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestEndOfLineComments
{
    @Test
    fun endOfLineCommentWithoutLineBreak()
    {
        val inputText = "// end of line comment"
        val inputStartPos = 1 + (Math.random() * 1000).toInt()

        val expectedComment = "// end of line comment"

        @Suppress("UnnecessaryVariable")
        val expectedStartPos = inputStartPos
        val expectedRemainingText = ""

        val actualResult = CommentExtractor.extract(inputText, inputStartPos)

        TestTools.assertAreEqual("Comment", actualResult.comment, expectedComment)
        MatcherAssert.assertThat("StartPos", actualResult.startPos, CoreMatchers.equalTo(expectedStartPos))
        TestTools.assertAreEqual("RemainingText", actualResult.remainingText, expectedRemainingText)
    }

    @Test
    fun endOfLineCommentWithLineBreak()
    {
        val inputText =
            "// end of line comment\n" +
            "abc();"
        val inputStartPos = 1 + (Math.random() * 1000).toInt()

        val expectedComment = "// end of line comment\n"

        @Suppress("UnnecessaryVariable")
        val expectedStartPos = inputStartPos
        val expectedRemainingText = "abc();"

        val actualResult = CommentExtractor.extract(inputText, inputStartPos)

        TestTools.assertAreEqual("Comment", actualResult.comment, expectedComment)
        MatcherAssert.assertThat("StartPos", actualResult.startPos, CoreMatchers.equalTo(expectedStartPos))
        TestTools.assertAreEqual("RemainingText", actualResult.remainingText, expectedRemainingText)
    }
}
