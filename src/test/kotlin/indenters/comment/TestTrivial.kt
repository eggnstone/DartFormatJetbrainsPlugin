package indenters.comment

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.CommentIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun whitespaceThrowsException()
    {
        val inputPart = Whitespace("")

        assertThrows<DartFormatException> { CommentIndenter(4).indentPart(inputPart) }
    }

    @Test
    fun comment()
    {
        val inputText = "comment"
        val inputPart = Comment(inputText)

        @Suppress("UnnecessaryVariable")
        val expectedText = inputText

        val actualText = CommentIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
