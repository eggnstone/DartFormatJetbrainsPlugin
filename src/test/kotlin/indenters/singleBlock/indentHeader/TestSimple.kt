package indenters.singleBlock.indentHeader

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.SingleBlockIndenter
import org.junit.Test

class TestSimple
{
    @Test
    fun oneLine()
    {
        val inputText = "class C {"

        val expectedText = "class C {"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun twoLines()
    {
        val inputText =
            "class C\n" +
            "{"

        val expectedText =
            "class C\n" +
            "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun threeLines()
    {
        val inputText =
            "class C\n" +
            "with X\n" +
            "{"

        val expectedText =
            "class C\n" +
            "    with X\n" +
            "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun voidMainAsync()
    {
        val inputText =
            "void main()\n" +
            "async\n" +
            "{"

        val expectedText =
            "void main()\n" +
            "async\n" +
            "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun annotation()
    {
        val inputText =
            "@annotation\n" +
            "void main()\n" +
            "{"

        val expectedText =
            "@annotation\n" +
            "void main()\n" +
            "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun annotations()
    {
        val inputText =
            "@annotation1\n" +
            "@annotation2\n" +
            "void main()\n" +
            "{"

        val expectedText =
            "@annotation1\n" +
            "@annotation2\n" +
            "void main()\n" +
            "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
