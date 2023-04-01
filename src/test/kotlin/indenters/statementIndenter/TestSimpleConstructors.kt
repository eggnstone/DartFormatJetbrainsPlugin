package indenters.statementIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.junit.Test

class TestSimpleConstructors
{
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

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun constructorWithAssignmentSplit()
    {
        val inputText =
            "C()\n" +
            ":\n" +
            "a = b;"
        val inputPart = Statement(inputText)

        val expectedText =
            "C()\n" +
            "    :\n" +
            "      a = b;"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun constructorWithAssignments()
    {
        val inputText =
            "C()\n" +
            ": a = b,\n" +
            "c = d;"
        val inputPart = Statement(inputText)

        val expectedText =
            "C()\n" +
            "    : a = b,\n" +
            "      c = d;"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun constructorWithAssignmentsSplit()
    {
        val inputText =
            "C()\n" +
            ":\n" +
            "a = b,\n" +
            "c = d;"
        val inputPart = Statement(inputText)

        val expectedText =
            "C()\n" +
            "    :\n" +
            "      a = b,\n" +
            "      c = d;"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
