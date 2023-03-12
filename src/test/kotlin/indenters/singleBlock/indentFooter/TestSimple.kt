package indenters.singleBlock.indentFooter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.SingleBlockIndenter
import org.junit.Test

class TestSimple
{
    @Test
    fun closingBraceOnly()
    {
        val inputText = "}"

        val expectedText = "}"

        val actualText = SingleBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    /*@Test
    fun closingBraceAndSpaceOnly()
    {
        val inputText = "} "

        val expectedText = "}"

        val actualText = SingleBlockIndenter(4).indentFooter(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }*/
}
