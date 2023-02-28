package indenters.singleBlock

import TestTools.Companion.assertAreEqual
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.SingleBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun whitespaceThrowsException()
    {
        val inputPart = Whitespace("")

        assertThrows<DartFormatException> { SingleBlockIndenter().indentPart(inputPart) }
    }

    @Test
    fun singleBlock()
    {
        val inputPart = SingleBlock("header{", "}footer")

        val expectedText = "header{}footer"

        val actualText = SingleBlockIndenter().indentPart(inputPart)

        assertAreEqual(actualText, expectedText)
    }
}
