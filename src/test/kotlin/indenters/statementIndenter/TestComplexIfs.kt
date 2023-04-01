package indenters.statementIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.junit.Test

class TestComplexIfs
{
    @Test
    fun twoNestedIfs()
    {
        val inputPart = Statement("if (true)\nif (true)\nabc();")

        val expectedText =
            "if (true)\n" +
            "    if (true)\n" +
            "        abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun twoNestedIfsInOneLine()
    {
        val inputPart = Statement("if (true) if (true)\nabc();")

        val expectedText =
            "if (true) if (true)\n" +
            "        abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun threeNestedIfs()
    {
        val inputPart = Statement("if (true)\nif (true)\nif (true)\nabc();")

        val expectedText =
            "if (true)\n" +
            "    if (true)\n" +
            "        if (true)\n" +
            "            abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun threeNestedIfsInOneLine()
    {
        val inputPart = Statement("if (true) if (true) if (true)\nabc();")

        val expectedText =
            "if (true) if (true) if (true)\n" +
            "            abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
