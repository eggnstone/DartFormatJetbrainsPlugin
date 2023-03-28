package indenters.blockIndenter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.BlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestSwitch
{
    @Test
    fun switchCases()
    {
        val inputParts = listOf(
            Whitespace("\n    "),
            Statement("case 0:\n        a();"),
            Whitespace("\n        "),
            Statement("break;"),
            Whitespace("\n    "),
            Statement("case 1:\n        return;"),
            Whitespace("\n    "),
            Statement("default:\n        break;"),
            Whitespace("\n")
        )

        val spacer = "    "
        val expectedText =
            "\n" +
                spacer + "case 0:\n" +
                spacer + "    a();\n" +
                spacer + "    break;\n" +
                spacer + "case 1:\n" +
                spacer + "    return;\n" +
                spacer + "default:\n" +
                spacer + "    break;\n"

        val actualText = BlockIndenter(4).indentParts(inputParts)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
