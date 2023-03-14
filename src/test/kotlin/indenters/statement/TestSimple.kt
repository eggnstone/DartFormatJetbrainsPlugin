package indenters.statement

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.junit.Test

class TestSimple
{
    @Test
    fun simpleIf()
    {
        val inputPart = Statement("if\nabc();")

        val expectedText =
            "if\n" +
            "    abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun ifWithSpaceAtTheEnd()
    {
        val inputPart = Statement("if \nabc();")

        val expectedText =
            "if\n" +
            "    abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun ifWithRoundBracketsWithoutSpace()
    {
        val inputPart = Statement("if()\nabc();")

        val expectedText =
            "if()\n" +
            "    abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun ifWithRoundBracketsWithSpace()
    {
        val inputPart = Statement("if ()\nabc();")

        val expectedText =
            "if ()\n" +
            "    abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun constructorWithAssignment()
    {
        val inputText =
            "C()\n" +
            ": a = b;"
        val inputPart = Statement(inputText)

        val expectedText =
            "C()\n" +
            "    : a = b;"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("Text", actualText, expectedText)
    }

    @Test
    fun constructorWithAssignments()
    {
        val inputText =
            "C()\n" +
            ": a = b,\n" +
            "a = b;"
        val inputPart = Statement(inputText)

        val expectedText =
            "C()\n" +
            "    : a = b,\n" +
            "      a = b;"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("Text", actualText, expectedText)
    }
}
