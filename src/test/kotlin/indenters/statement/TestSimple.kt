package indenters.statement

import TestTools.Companion.assertAreEqual
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.junit.Test

class TestSimple
{
    @Test
    fun simpleIf()
    {
        val inputPart = Statement("if\nabc();")

        val expectedText = "if\n" +
        "    abc();"

        val actualText = StatementIndenter().indentPart(inputPart)

        assertAreEqual(actualText, expectedText)
    }
}
