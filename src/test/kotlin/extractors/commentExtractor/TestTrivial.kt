package extractors.commentExtractor

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        val expectedComment = ""
        val expectedRemainingText = ""

        val actualResult = CommentExtractor.extract(inputText)

        TestTools.assertAreEqual(actualResult.comment, expectedComment)
        TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
    }

    @Test
    fun noComment()
    {
        val inputText = "abc();"

        assertThrows<DartFormatException> { CommentExtractor.extract(inputText) }
    }
}
