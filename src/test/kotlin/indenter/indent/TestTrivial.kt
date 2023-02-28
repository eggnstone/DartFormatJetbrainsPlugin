package indenter.indent

import dev.eggnstone.plugins.jetbrains.dartformat.Indenter
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

        val actualText = Indenter().indent(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
