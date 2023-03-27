package indenters.whitespaceIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.WhitespaceIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestSimple
{
    companion object
    {
        const val START_INDENT = 0
    }

    @Test
    fun space()
    {
        val inputPart = Whitespace(" ")

        val expectedText = ""

        val actualText = WhitespaceIndenter().indentPart(inputPart, START_INDENT)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun spaces()
    {
        val inputPart = Whitespace("  ")

        val expectedText = ""

        val actualText = WhitespaceIndenter().indentPart(inputPart, START_INDENT)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun lineBreak()
    {
        val inputPart = Whitespace("\n")

        val expectedText = "\n"

        val actualText = WhitespaceIndenter().indentPart(inputPart, START_INDENT)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun mixed()
    {
        val inputPart = Whitespace(" \n\r\t ")

        val expectedText = "\n\r"

        val actualText = WhitespaceIndenter().indentPart(inputPart, START_INDENT)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
