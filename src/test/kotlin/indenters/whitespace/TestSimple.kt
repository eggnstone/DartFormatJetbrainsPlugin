package indenters.whitespace

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.WhitespaceIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestSimple
{
    @Test
    fun space()
    {
        val inputPart = Whitespace(" ")

        val expectedText = ""

        val actualText = WhitespaceIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun lineBreak()
    {
        val inputPart = Whitespace("\n")

        val expectedText = "\n"

        val actualText = WhitespaceIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun mixed()
    {
        val inputPart = Whitespace(" \n\r\t ")

        val expectedText = "\n\r"

        val actualText = WhitespaceIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
