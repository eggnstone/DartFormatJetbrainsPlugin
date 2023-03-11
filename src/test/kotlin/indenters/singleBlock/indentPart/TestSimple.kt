package indenters.singleBlock.indentPart

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.SingleBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestSimple
{
    @Test
    fun singleBlock()
    {
        val inputPart = SingleBlock("class C\n{", "}")

        val expectedText =
            "class C\n" +
            "{}"

        val actualText = SingleBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun singleBlockWithLineBreak()
    {
        val inputPart = SingleBlock("class C\n{", "}", listOf(Whitespace("\n")))

        val expectedText =
            "class C\n" +
            "{\n" +
            "}"

        val actualText = SingleBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun singleBlockWithMultiLineHeader()
    {
        val inputPart = SingleBlock("class C\nwith X\n{", "}")

        val expectedText =
            "class C\n" +
            "    with X\n" +
            "{}"

        val actualText = SingleBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
