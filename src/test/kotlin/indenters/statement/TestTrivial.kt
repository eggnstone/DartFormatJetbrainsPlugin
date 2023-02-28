package indenters.statement

import TestTools.Companion.assertAreEqual
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun whitespaceThrowsException()
    {
        val inputPart = Whitespace("")

        assertThrows<DartFormatException> { StatementIndenter().indentPart(inputPart) }
    }

    @Test
    fun statement()
    {
        val inputPart = Statement("abc();")

        val expectedText = "abc();"

        val actualText = StatementIndenter().indentPart(inputPart)

        assertAreEqual(actualText, expectedText)
    }
}
