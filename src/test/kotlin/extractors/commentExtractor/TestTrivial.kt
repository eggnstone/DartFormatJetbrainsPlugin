package extractors.commentExtractor

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""
        val inputStartPos = 1 + (Math.random() * 1000).toInt()

        val expectedComment = ""

        @Suppress("UnnecessaryVariable")
        val expectedStartPos = inputStartPos
        val expectedRemainingText = ""

        val actualResult = CommentExtractor.extract(inputText, inputStartPos)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        MatcherAssert.assertThat(actualResult.startPos, CoreMatchers.equalTo(expectedStartPos))
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }

    @Test
    fun noComment()
    {
        val inputText = "abc();"

        assertThrows<DartFormatException> { CommentExtractor.extract(inputText, 0) }
    }
}
