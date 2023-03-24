package indenters.master.indentParts

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestComplex
{
    @Test
    fun statementAndWhitespaceAndStatement()
    {
        val inputParts = listOf(Statement("abc();"), Whitespace("    "), Statement("def();"))

        val expectedText = "abc(); def();"

        val actualText = MasterIndenter(4).indentParts(inputParts)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
