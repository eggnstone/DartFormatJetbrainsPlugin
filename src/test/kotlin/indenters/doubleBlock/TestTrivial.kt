package indenters.doubleBlock

import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestTrivial
{

    @Test
    fun doubleBlock()
    {
        val inputPart = DoubleBlock("header{", "}middle{", "}footer")

        val expectedText = "header{}middle{}footer"

        val actualText = MasterIndenter().indentPart(inputPart)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
