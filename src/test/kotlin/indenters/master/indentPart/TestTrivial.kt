package indenters.master.indentPart

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test

class TestTrivial
{
    @Test
    fun coverage()
    {
        val inputPart = Whitespace("")

        val expectedText = ""

        val actualText = MasterIndenter().indentPart(inputPart)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
