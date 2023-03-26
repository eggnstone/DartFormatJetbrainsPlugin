package indenters.statement

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.junit.Test

class TestSimpleIfs
{
    @Test
    fun simpleIf()
    {
        val inputPart = Statement("if\nabc();")

        val expectedText =
            "if\n" +
                "    abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun ifWithSpaceAtTheEnd()
    {
        val inputPart = Statement("if \nabc();")

        val expectedText =
            "if\n" +
                "    abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun ifWithRoundBracketsWithoutSpace()
    {
        val inputPart = Statement("if()\nabc();")

        val expectedText =
            "if()\n" +
                "    abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun ifWithRoundBracketsWithSpace()
    {
        val inputPart = Statement("if ()\nabc();")

        val expectedText =
            "if ()\n" +
                "    abc();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun simpleIfAndElse()
    {
        val inputPart = Statement("if (a)\na();\nelse\nb();")

        val expectedText =
            "if (a)\n" +
                "    a();\n" +
                "else\n" +
                "    b();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun simpleIfAndElseIf()
    {
        val inputPart = Statement("if (a)\na();\nelse if (b)\nb();")

        val expectedText =
            "if (a)\n" +
                "    a();\n" +
                "else if (b)\n" +
                "    b();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("",actualText, expectedText)
    }

    @Test
    fun simpleIfAndElseIfAndElse()
    {
        val inputPart = Statement("if (a)\na();\nelse if (b)\nb();\nelse\nc();")

        val expectedText =
            "if (a)\n" +
                "    a();\n" +
                "else if (b)\n" +
                "    b();\n" +
                "else\n" +
                "    c();"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
