package indenters.statement

import TestTools.Companion.assertAreEqual
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.junit.Test

class TestComplex
{
    @Test
    fun twoNestedIfs()
    {
        val inputPart = Statement("if (true)\nif (true)\nabc();")

        val expectedText = "if (true)\n" +
        "    if (true)\n" +
        "        abc();"

        val actualText = StatementIndenter().indentPart(inputPart)

        assertAreEqual(actualText, expectedText)
    }

    @Test
    fun twoNestedIfsInOneLine()
    {
        val inputPart = Statement("if (true) if (true)\nabc();")

        val expectedText = "if (true) if (true)\n" +
        "        abc();"

        val actualText = StatementIndenter().indentPart(inputPart)

        assertAreEqual(actualText, expectedText)
    }

    @Test
    fun threeNestedIfs()
    {
        val inputPart = Statement("if (true)\nif (true)\nif (true)\nabc();")

        val expectedText = "if (true)\n" +
        "    if (true)\n" +
        "        if (true)\n" +
        "            abc();"

        val actualText = StatementIndenter().indentPart(inputPart)

        assertAreEqual(actualText, expectedText)
    }

    @Test
    fun threeNestedIfsInOneLine()
    {
        val inputPart = Statement("if (true) if (true) if (true)\nabc();")

        val expectedText = "if (true) if (true) if (true)\n" +
        "            abc();"

        val actualText = StatementIndenter().indentPart(inputPart)

        assertAreEqual(actualText, expectedText)
    }
}