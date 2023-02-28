package indenters.master.indentParts

import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputParts = listOf<IPart>()

        val expectedText = ""

        val actualText = MasterIndenter().indentParts(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
