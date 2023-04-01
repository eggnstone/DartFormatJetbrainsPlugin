package indenters.commentIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.CommentIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestsParametrizedWithLevel(private val level: Int)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "Level: {0}")
        fun data() = listOf(0, 1, 2)

        const val DEFAULT_START_INDENT = 0
        const val SPACES_PER_LEVEL = 4
    }

    private val space: String = StringWrapper.getSpaces(level * SPACES_PER_LEVEL)

    @Test
    fun endOfLineComment()
    {
        val inputText = "// comment\n"
        val inputPart = Comment(inputText)

        @Suppress("UnnecessaryVariable")
        val expectedText = space + inputText

        val actualText = CommentIndenter(4).indentPart(inputPart, DEFAULT_START_INDENT, level)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun multiLineComment_singleLine()
    {
        val inputText = "/* comment */"
        val inputPart = Comment(inputText)

        @Suppress("UnnecessaryVariable")
        val expectedText = space + inputText

        val actualText = CommentIndenter(SPACES_PER_LEVEL).indentPart(inputPart, DEFAULT_START_INDENT, level)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun multiLineCommentKeepsFormatting_startPos0()
    {
        multiLineCommentKeepsFormattingWithStartPos(0)
    }

    @Test
    fun multiLineCommentKeepsFormatting_startPos1()
    {
        multiLineCommentKeepsFormattingWithStartPos(1)
    }

    @Test
    fun multiLineCommentKeepsFormatting_startPos2()
    {
        multiLineCommentKeepsFormattingWithStartPos(2)
    }

    @Test
    fun multiLineCommentKeepsFormatting_startPos3()
    {
        multiLineCommentKeepsFormattingWithStartPos(3)
    }

    private fun multiLineCommentKeepsFormattingWithStartPos(startPos: Int)
    {
        @Suppress("UnnecessaryVariable")
        val line0Indent = startPos
        val line1Indent = 3
        val line2Indent = 5

        val inputText =
            "/*\n" +
            StringWrapper.getSpaces(line1Indent) + "comment\n" +
            StringWrapper.getSpaces(line2Indent) + "*/"
        val inputPart = Comment(inputText, line0Indent)

        @Suppress("UnnecessaryVariable")
        val expectedText =
            space + "/*\n" +
            space + StringWrapper.getSpaces(line1Indent - line0Indent) + "comment\n" +
            space + StringWrapper.getSpaces(line2Indent - line0Indent) + "*/"

        val actualText = CommentIndenter(SPACES_PER_LEVEL).indentPart(inputPart, DEFAULT_START_INDENT, level)

        DotlinLogger.log("actualText:\n$actualText")

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
