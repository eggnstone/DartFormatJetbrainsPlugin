package indenters.statement

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.junit.Test

class TestSimple
{
    @Test
    fun simpleIf()
    {
        val inputPart = Statement("if\nabc();")

        val expectedText = "if\n" +
        "    abc();"

        val actualText = StatementIndenter().indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun ifWithSpaceAtTheEnd()
    {
        val inputPart = Statement("if \nabc();")

        val expectedText = "if\n" +
        "    abc();"

        val actualText = StatementIndenter().indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun ifWithRoundBracketsWithoutSpace()
    {
        val inputPart = Statement("if()\nabc();")

        val expectedText = "if()\n" +
        "    abc();"

        val actualText = StatementIndenter().indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun ifWithRoundBracketsWithSpace()
    {
        val inputPart = Statement("if ()\nabc();")

        val expectedText = "if ()\n" +
        "    abc();"

        val actualText = StatementIndenter().indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
