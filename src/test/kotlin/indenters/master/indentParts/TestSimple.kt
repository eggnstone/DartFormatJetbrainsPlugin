package indenters.master.indentParts

import TestTools.Companion.assertAreEqual
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestSimple
{
    @Test
    fun whitespaceBeforeStatement()
    {
        val inputParts = listOf(Whitespace("\n\r\t "), Statement("abc();"))

        val expectedText = "\n\rabc();"

        val actualText = MasterIndenter().indentParts(inputParts)

        assertAreEqual(actualText, expectedText)
    }

    @Test
    fun whitespaceAfterStatement()
    {
        val inputParts = listOf(Statement("abc();"), Whitespace("\n\r\t "))

        val expectedText = "abc();\n\r"

        val actualText = MasterIndenter().indentParts(inputParts)

        assertAreEqual(actualText, expectedText)
    }

    @Test
    fun whitespaceBeforeAndAfterStatement()
    {
        val inputParts = listOf(Whitespace("\n\r\t "), Statement("abc();"), Whitespace("\n\r\t "))

        val expectedText = "\n\rabc();\n\r"

        val actualText = MasterIndenter().indentParts(inputParts)

        assertAreEqual(actualText, expectedText)
    }

    @Test
    fun statementsWithoutWhitespace()
    {
        val inputParts = listOf(Statement("abc();"), Statement("def();"))

        val expectedText = "abc();def();"

        val actualText = MasterIndenter().indentParts(inputParts)

        assertAreEqual(actualText, expectedText)
    }

    @Test
    fun whitespaceBetweenStatements()
    {
        val inputParts = listOf(Statement("abc();"), Whitespace("\n\r\t "), Statement("def();"))

        val expectedText = "abc();\n\rdef();"

        val actualText = MasterIndenter().indentParts(inputParts)

        assertAreEqual(actualText, expectedText)
    }
}
