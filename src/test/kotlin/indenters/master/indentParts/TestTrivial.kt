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

        val actualText = MasterIndenter().indentParts(inputParts)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
