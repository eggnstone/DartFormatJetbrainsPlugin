package indenters.blockIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.BlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestSimple
{
    @Test
    fun withLineBreakAtStart_indentFirst()
    {
        val lineBreakWhitespace = "\n"
        val statement = "STATEMENT;"
        val inputParts = listOf(Whitespace(lineBreakWhitespace), Statement(statement))

        val expectedIndent = "    "
        val expectedText = "$lineBreakWhitespace$expectedIndent$statement"

        val actualText = BlockIndenter(4).indentParts(inputParts)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun withSpaceAtStart_doNotIndentFirst()
    {
        val spaceWhitespace = " "
        val statement = "STATEMENT;"
        val inputParts = listOf(Whitespace(spaceWhitespace), Statement(statement))

        @Suppress("UnnecessaryVariable")
        val expectedText = statement

        val actualText = BlockIndenter(4).indentParts(inputParts)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun withoutLineBreakAtStart_doNotIndentFirst()
    {
        val statement = "STATEMENT;"
        val inputParts = listOf(Statement(statement))

        @Suppress("UnnecessaryVariable")
        val expectedText = statement

        val actualText = BlockIndenter(4).indentParts(inputParts)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
