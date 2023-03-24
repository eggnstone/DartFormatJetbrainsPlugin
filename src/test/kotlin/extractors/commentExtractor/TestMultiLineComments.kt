package extractors.commentExtractor

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestMultiLineComments
{
    @Test
    fun multiLineCommentWithoutLineBreak()
    {
        val inputText = "/* multi line comment */"
        val inputStartPos = 1 + (Math.random() * 1000).toInt()

        val expectedComment = "/* multi line comment */"

        @Suppress("UnnecessaryVariable")
        val expectedStartPos = inputStartPos
        val expectedRemainingText = ""

        val actualResult = CommentExtractor.extract(inputText, inputStartPos)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        MatcherAssert.assertThat(actualResult.startPos, CoreMatchers.equalTo(expectedStartPos))
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }

    @Test
    fun multiLineCommentWithLineBreak()
    {
        val inputText =
            "/* multi line comment */\n" +
                "abc();"
        val inputStartPos = 1 + (Math.random() * 1000).toInt()

        val expectedComment = "/* multi line comment */"

        @Suppress("UnnecessaryVariable")
        val expectedStartPos = inputStartPos
        val expectedRemainingText =
            "\n" +
                "abc();"

        val actualResult = CommentExtractor.extract(inputText, inputStartPos)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        MatcherAssert.assertThat(actualResult.startPos, CoreMatchers.equalTo(expectedStartPos))
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }

    @Test
    fun multiLineCommentWithLineBreaks()
    {
        val inputText =
            "/* multi line\n" +
                "comment */\n" +
                "abc();"
        val inputStartPos = 1 + (Math.random() * 1000).toInt()

        val expectedComment =
            "/* multi line\n" +
                "comment */"

        @Suppress("UnnecessaryVariable")
        val expectedStartPos = inputStartPos
        val expectedRemainingText =
            "\n" +
                "abc();"

        val actualResult = CommentExtractor.extract(inputText, inputStartPos)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        MatcherAssert.assertThat(actualResult.startPos, CoreMatchers.equalTo(expectedStartPos))
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }

    @Test
    fun multiLineCommentWithLineBreaksAndStartPos()
    {
        val inputText =
            "/* multi line\n" +
                "comment */\n" +
                "abc();"
        val inputStartPos = 1 + (Math.random() * 1000).toInt()

        val expectedComment =
            "/* multi line\n" +
                "comment */"

        @Suppress("UnnecessaryVariable")
        val expectedStartPos = inputStartPos
        val expectedRemainingText =
            "\n" +
                "abc();"

        val actualResult = CommentExtractor.extract(inputText, inputStartPos)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        MatcherAssert.assertThat(actualResult.startPos, CoreMatchers.equalTo(expectedStartPos))
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }

    @Test
    fun multiLineCommentWithSpacesAtEndOfLine()
    {
        val inputText =
            "/* multi line    \n" +
                "comment */"
        val inputStartPos = 1 + (Math.random() * 1000).toInt()

        val expectedComment =
            "/* multi line\n" +
                "comment */"

        @Suppress("UnnecessaryVariable")
        val expectedStartPos = inputStartPos
        val expectedRemainingText = ""

        val actualResult = CommentExtractor.extract(inputText, inputStartPos)

        TestTools.assertAreEqual("comment", actualResult.comment, expectedComment)
        MatcherAssert.assertThat("startPos", actualResult.startPos, CoreMatchers.equalTo(expectedStartPos))
        TestTools.assertAreEqual("remainingText", actualResult.remainingText, expectedRemainingText)
    }
}
