package indenters.master.indentPart

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestTrivial
{
    @Test
    fun coverage()
    {
        val inputPart = Whitespace("")

        val expectedText = ""

        val actualText = MasterIndenter(4).indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
