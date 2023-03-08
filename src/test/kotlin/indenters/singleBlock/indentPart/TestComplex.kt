package indenters.singleBlock.indentPart

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.SingleBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestComplex
{
    @Test
    fun singleBlockWithLineBreaks()
    {
        val inputPart =
            SingleBlock(
                "if (true)\n" +
                "{",
                "}",
                listOf(Whitespace("\n"), Statement("abc();"), Whitespace("\n"))
            )

        val expectedText = "if (true)\n" +
        "{\n" +
        "    abc();\n" +
        "}"

        val actualText = SingleBlockIndenter().indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
