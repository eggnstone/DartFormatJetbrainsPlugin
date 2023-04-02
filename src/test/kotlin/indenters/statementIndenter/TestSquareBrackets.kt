package indenters.statementIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.junit.Test

class TestSquareBrackets
{
    @Test
    fun withLastComma()
    {
        val statement = Statement("[\nif (a) b,\nif (c) d,\n]")

        val expectedText = "[\n    if (a) b,\n    if (c) d,\n]"

        val actualText = StatementIndenter(4).indentPart(statement)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun withoutLastComma()
    {
        val statement = Statement("[\nif (a) b,\nif (c) d\n]")

        val expectedText = "[\n    if (a) b,\n    if (c) d\n]"

        val actualText = StatementIndenter(4).indentPart(statement)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
