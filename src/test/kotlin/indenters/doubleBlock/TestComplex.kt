package indenters.doubleBlock

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.DoubleBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestComplex
{
    @Test
    fun doubleBlockWithLineBreaks()
    {
        val inputPart =
            DoubleBlock(
                "if (true)\n" +
                "{", "}\n" +
                "else\n" +
                "{",
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

        val actualText = DoubleBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
