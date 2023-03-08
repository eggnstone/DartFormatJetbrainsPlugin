package extractors.commentExtractor

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import org.junit.Test

class TestMultiLineComments
{
    @Test
    fun multiLineCommentWithoutLineBreak()
    {
        val inputText = "/* multi line comment */"

        val expectedComment = "/* multi line comment */"
        val expectedRemainingText = ""

        val actualResult = CommentExtractor.extract(inputText)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }

    @Test
    fun multiLineCommentWithLineBreak()
    {
        val inputText = "/* multi line comment */\n" +
        "abc();"

        val expectedComment = "/* multi line comment */"
        val expectedRemainingText = "\n" +
        "abc();"

        val actualResult = CommentExtractor.extract(inputText)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }

    @Test
    fun multiLineCommentWithLineBreak2()
    {
        val inputText = "/* multi line\n" +
        "comment */\n" +
        "abc();"

        val expectedComment = "/* multi line\n" +
        "comment */"
        val expectedRemainingText = "\n" +
        "abc();"

        val actualResult = CommentExtractor.extract(inputText)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }
}
