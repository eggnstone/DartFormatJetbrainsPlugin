package indenters.singleBlock.indentHeader

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.SingleBlockIndenter
import org.junit.Ignore
import org.junit.Test

class TestTrivial
{
    @Test
    fun emptyHeader()
    {
        val inputText = ""

        val expectedText = ""

        val actualText = SingleBlockIndenter().indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    @Ignore
    fun spaceTODO()
    {
        val inputText = " "

        val expectedText = ""

        val actualText = SingleBlockIndenter().indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
