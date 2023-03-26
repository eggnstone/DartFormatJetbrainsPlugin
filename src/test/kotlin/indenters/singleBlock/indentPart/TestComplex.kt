package indenters.singleBlock.indentPart

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestComplex
{
    @Test
    fun ifBlockWithLineBreaks()
    {
        val inputPart =
            MultiBlock.single(
                "if (true)\n{",
                "}",
                listOf(Whitespace("\n"), Statement("abc();"), Whitespace("\n"))
            )

        val expectedText =
            "if (true)\n" +
                "{\n" +
                "    abc();\n" +
                "}"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun ifBlockAndElseStatement()
    {
        val inputPart =
            MultiBlock.single(
                "if (true)\n{",
                "}\nelse\ndef();",
                listOf(Whitespace("\n"), Statement("abc();"), Whitespace("\n"))
            )

        val expectedText =
            "if (true)\n" +
                "{\n" +
                "    abc();\n" +
                "}\n" +
                "else\n" +
                "    def();"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
