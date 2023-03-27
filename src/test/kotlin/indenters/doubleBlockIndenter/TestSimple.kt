package indenters.doubleBlockIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestSimple
{
    @Test
    fun doubleBlock()
    {
        val inputPart =
            MultiBlock.double(
                "if (true)\n{",
                "}\nelse\n{",
                "}"
            )

        val expectedText =
            "if (true)\n" +
                "{}\n" +
                "else\n" +
                "{}"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun doubleBlockWithLineBreaks()
    {
        val inputPart =
            MultiBlock.double(
                "if (true)\n{",
                "}\nelse\n{",
                "}",
                listOf<IPart>(Whitespace("\n")),
                listOf<IPart>(Whitespace("\n"))
            )

        val expectedText =
            "if (true)\n" +
                "{\n" +
                "}\n" +
                "else\n" +
                "{\n" +
                "}"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
