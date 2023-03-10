package indenters.whitespace

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.WhitespaceIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun nonWhitespaceThrowsException()
    {
        val inputPart = Statement("")

        assertThrows<DartFormatException> { WhitespaceIndenter(4).indentPart(inputPart) }
    }

    @Test
    fun emptyText()
    {
        val inputPart = Whitespace("")

        val expectedText = ""

        val actualText = WhitespaceIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
