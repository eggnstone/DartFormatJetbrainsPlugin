package indenters.doubleBlockIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestComplex
{
    @Test
    fun doubleBlockWithLineBreaks()
    {
        val inputPart =
            MultiBlock.double(
                "if (true)\n{",
                "}\nelse\n{",
                "}",
                listOf(Whitespace("\n"), Statement("abc();"), Whitespace("\n")),
                listOf(Whitespace("\n"), Statement("def();"), Whitespace("\n"))
            )

        val expectedText =
            "if (true)\n" +
            "{\n" +
            "    abc();\n" +
            "}\n" +
            "else\n" +
            "{\n" +
            "    def();\n" +
            "}"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
