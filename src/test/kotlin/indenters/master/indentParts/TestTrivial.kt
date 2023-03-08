package indenters.master.indentParts

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import org.junit.Test

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputParts = listOf<IPart>()

        val expectedText = ""

        val actualText = MasterIndenter(4).indentParts(inputParts)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
