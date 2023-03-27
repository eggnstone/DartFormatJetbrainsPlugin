package indenters.multiBlockIndenter.indentHeader

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import org.junit.Test

class TestSimpleIsNotFirst
{
    @Test
    fun doNotIndentBlockAfterElse()
    {
        val inputText = "}\nelse\n{"

        val expectedText = "}\nelse\n{"

        val actualText = MultiBlockIndenter(4).indentHeader(inputText, isFirst = false)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    //"}\nelse if (b)\nbs();\nelse\n{"

    @Test
    fun indentAfterElse_doNotIndentBlockAfterElseIfElse()
    {
        val inputText = "}\nelse if (b)\nb();\nelse\n{"

        val expectedText = "}\nelse if (b)\n    b();\nelse\n{"

        val actualText = MultiBlockIndenter(4).indentHeader(inputText, isFirst = false)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
