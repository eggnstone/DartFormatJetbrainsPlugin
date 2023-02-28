package indenters.doubleBlock

import TestTools.Companion.assertAreEqual
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.DoubleBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun whitespaceThrowsException()
    {
        val inputPart = Whitespace("")

        assertThrows<DartFormatException> { DoubleBlockIndenter().indentPart(inputPart) }
    }

    @Test
    fun doubleBlock()
    {
        val inputPart = DoubleBlock("header{", "}middle{", "}footer")

        val expectedText = "header{}middle{}footer"

        val actualText = DoubleBlockIndenter().indentPart(inputPart)

        assertAreEqual(actualText, expectedText)
    }
}
