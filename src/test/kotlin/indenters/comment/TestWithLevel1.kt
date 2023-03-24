package indenters.comment

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.CommentIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import org.junit.Test

class TestWithLevel1
{
    @Test
    fun endOfLineComment()
    {
        val inputText = "// comment\n"
        val inputPart = Comment(inputText)

        @Suppress("UnnecessaryVariable")
        val expectedText = "    $inputText"

        val actualText = CommentIndenter(4).indentPart(inputPart, 1)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun multiLineComment_singleLine()
    {
        val inputText = "/* comment */"
        val inputPart = Comment(inputText)

        @Suppress("UnnecessaryVariable")
        val expectedText = "    $inputText"

        val actualText = CommentIndenter(4).indentPart(inputPart, 1)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun multiLineCommentKeepsFormatting()
    {
        val inputText =
            "/*\n" +
                "   comment\n" +
                "       */"
        val inputPart = Comment(inputText)

        @Suppress("UnnecessaryVariable")
        val expectedText =
            "    /*\n" +
                "       comment\n" +
                "           */"

        val actualText = CommentIndenter(4).indentPart(inputPart, 1)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
