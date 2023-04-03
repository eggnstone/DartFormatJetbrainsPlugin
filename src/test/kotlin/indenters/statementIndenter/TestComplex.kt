package indenters.statementIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.junit.Test

class TestComplex
{
    @Test
    fun statementWithBlockInsideRoundBrackets()
    {
        val inputPart = Statement("a(\n{\ns;\n});")

        val expectedText =
            "a(\n" +
            "{\n" +
            "    s;\n" +
            "});"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun statementWithBlockInsideRoundBrackets2()
    {
        val inputPart = Statement("a((\n{\ns;\n}));")

        val expectedText =
            "a((\n" +
            "{\n" +
            "    s;\n" +
            "}));"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun statementWithBlockInsideRoundBrackets22()
    {
        val inputPart = Statement("a(\nb\n{\ns;\n});")

        val expectedText =
            "a(\n" +
            "    b\n" +
            "    {\n" +
            "        s;\n" +
            "});"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
