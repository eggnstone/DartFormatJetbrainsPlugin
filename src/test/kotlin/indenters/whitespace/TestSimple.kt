package indenters.whitespace

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.WhitespaceIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestSimple
{
    @Test
    fun emptyText()
    {
        val inputPart = Whitespace("\n\r\t ")

        val expectedText = ""

        val actualText = WhitespaceIndenter().indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
