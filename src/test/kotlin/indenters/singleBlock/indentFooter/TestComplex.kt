package indenters.singleBlock.indentFooter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.SingleBlockIndenter
import org.junit.Test

class TestComplex
{
    @Test
    fun oneLine()
    {
        val inputText = "} else abc();"

        val expectedText = "} else abc();"

        val actualText = SingleBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun twoLines()
    {
        val inputText =
            "} else\n" +
            "abc();"

        val expectedText =
            "} else\n" +
            "    abc();"

        val actualText = SingleBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun threeLines()
    {
        val inputText =
            "}\n" +
            "else\n" +
            "abc();"

        val expectedText =
            "}\n" +
            "else\n" +
            "    abc();"

        val actualText = SingleBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
