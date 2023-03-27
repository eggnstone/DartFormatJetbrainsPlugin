package indenters.multiBlock.indentFooter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import org.junit.Test

class TestSimple
{
    @Test
    fun closingBraceOnly()
    {
        val inputText = "}"

        val expectedText = "}"

        val actualText = MultiBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    /*@Test
    fun closingBraceAndSpaceOnly()
    {
        val inputText = "} "

        val expectedText = "}"

        val actualText = MultiBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }*/

    @Test
    fun doNotIndentElse()
    {
        val inputText = "}\nelse\nb();"

        val expectedText = "}\nelse\n    b();"

        val actualText = MultiBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun doNotIndentElseIfElse()
    {
        val inputText = "}\nelse if (b)\nb();\nelse\nc();"

        val expectedText = "}\nelse if (b)\n    b();\nelse\n    c();"

        val actualText = MultiBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun doNotIndentBlockAfterElse()
    {
        val inputText = "}\nelse\n{"

        val expectedText = "}\nelse\n{"

        val actualText = MultiBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun indentAfterElse_doNotIndentBlockAfterElseIfElse()
    {
        val inputText = "}\nelse if (b)\nb();\nelse\n{"

        val expectedText = "}\nelse if (b)\n    b();\nelse\n{"

        val actualText = MultiBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
