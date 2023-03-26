package indenters.singleBlock

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestSimple
{
    @Test
    fun singleBlockWithSpace()
    {
        val inputPart = MultiBlock.single("class C {", "}")

        val expectedText = "class C {}"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun singleBlockWithSpaceAndAnnotation()
    {
        val inputPart = MultiBlock.single("@annotation\ntext {", "}")

        val expectedText = "@annotation\ntext {}"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun singleBlockWithLineBreak()
    {
        val inputPart = MultiBlock.single("class C\n{", "}")

        val expectedText =
            "class C\n" +
                "{}"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun singleBlockWithLineBreakInside()
    {
        val inputPart = MultiBlock.single("class C\n{", "}", listOf(Whitespace("\n")))

        val expectedText =
            "class C\n" +
                "{\n" +
                "}"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun singleBlockWithMultiLineHeader()
    {
        val inputPart = MultiBlock.single("class C\nwith X\n{", "}")

        val expectedText =
            "class C\n" +
                "    with X\n" +
                "{}"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
