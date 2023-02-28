package indenters.singleBlock

import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestTrivial
{

    @Test
    fun singleBlock()
    {
        val inputPart = SingleBlock("header{", "}footer")

        val expectedText = "header{}footer"

        val actualText = MasterIndenter().indentPart(inputPart)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
