package indenters.masterIndenter.indentParts

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Ignore
import org.junit.Test

class TestComplex
{
    @Test
    fun statementAndWhitespaceAndStatement()
    {
        val inputParts = listOf(Statement("abc();"), Whitespace("    "), Statement("def();"))

        val expectedText = "abc(); def();"

        val actualText = MasterIndenter(4).indentParts(inputParts)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun statementAndWhitespaceMultiLineComment_correctStartPos()
    {
        val inputParts = listOf(Statement("abc();"), Whitespace("    "), Comment("/*\n  */", 7))

        val expectedText = "abc(); /*\n  */"

        val actualText = MasterIndenter(4).indentParts(inputParts)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    @Ignore
    fun statementAndWhitespaceMultiLineComment_incorrectStartPos()
    {
        val inputParts = listOf(Statement("abc();"), Whitespace("    "), Comment("/*\n  */", 20))

        val expectedText = "abc(); /*\n  */"

        val actualText = MasterIndenter(4).indentParts(inputParts)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
