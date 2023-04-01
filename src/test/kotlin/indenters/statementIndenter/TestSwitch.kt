package indenters.statementIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.StatementIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.junit.Test

class TestSwitch
{
    @Test
    fun caseAndBreak()
    {
        val inputPart = Statement("case:\ns();\nbreak;")

        val expectedText =
            "case:\n" +
            "    s();\n" +
            "    break;"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun defaultAndBreak()
    {
        val inputPart = Statement("default:\ns();\nbreak;")

        val expectedText =
            "default:\n" +
            "    s();\n" +
            "    break;"

        val actualText = StatementIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
