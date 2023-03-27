package indenters.multiBlockIndenter.indentPart

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestSimple
{
    @Test
    fun headerWithLineBreak_indentStartOfBlock()
    {
        val header = "HEADER{"
        val whitespace = "\n"
        val statement = "STATEMENT;"
        val footer = "}FOOTER"

        val inputPart = MultiBlock(listOf(header), listOf(listOf(Whitespace(whitespace), Statement(statement))), footer)

        val expectedIndent = "    "
        val expectedText = "$header$whitespace$expectedIndent$statement$footer"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun headerWithoutLineBreak_doNotIndentStartOfBlock()
    {
        val header = "HEADER{"
        val statement = "STATEMENT;"
        val footer = "}FOOTER"

        val inputPart = MultiBlock(listOf(header), listOf(listOf(Statement(statement))), footer)

        val expectedText = header + statement + footer

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}