package indenters.singleBlockIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestIfs
{
    @Test
    fun oneIf()
    {
        val inputPart = MultiBlock.single("if (a)\n{", "}", listOf(Whitespace("\n"), Statement("s();\n")))

        val expectedText = "if (a)\n{\n    s();\n}"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun twoIfs()
    {
        val inputPart = MultiBlock.single("if (a)\nif (b)\n{", "}", listOf(Whitespace("\n"), Statement("s();\n")))

        val expectedText = "if (a)\n    if (b)\n    {\n        s();\n    }"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun threeIfs()
    {
        val inputPart = MultiBlock.single("if (a)\nif (b)\nif (c)\n{", "}", listOf(Whitespace("\n"), Statement("s();\n")))

        val expectedText = "if (a)\n    if (b)\n        if (c)\n        {\n            s();\n        }"

        val actualText = MultiBlockIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
