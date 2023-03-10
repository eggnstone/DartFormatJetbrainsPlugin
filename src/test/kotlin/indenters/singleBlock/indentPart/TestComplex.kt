package indenters.singleBlock.indentPart

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.SingleBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestComplex
{
    @Test
    fun ifBlockWithLineBreaks()
    {
        val inputPart =
            SingleBlock(
                "if (true)\n" +
                "{",
                "}",
                listOf(Whitespace("\n"), Statement("abc();"), Whitespace("\n"))
            )

        val expectedText =
            "if (true)\n" +
            "{\n" +
            "    abc();\n" +
            "}"

        val actualText = SingleBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun ifBlockAndElseStatement()
    {
        val inputPart =
            SingleBlock(
                "if (true)\n" +
                "{",
                "}\n" +
                "else\n" +
                "def();",
                listOf(Whitespace("\n"), Statement("abc();"), Whitespace("\n"))
            )

        val expectedText =
            "if (true)\n" +
            "{\n" +
            "    abc();\n" +
            "}\n" +
            "else\n" +
            "    def();"

        val actualText = SingleBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
