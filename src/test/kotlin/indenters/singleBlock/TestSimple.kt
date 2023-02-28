package indenters.singleBlock

import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{

    @Test
    fun singleBlock()
    {
        val inputPart = SingleBlock("class C\n{", "}")

        val expectedText = "class C\n" +
        "{}"

        val actualText = MasterIndenter().indentPart(inputPart)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }

    @Test
    fun singleBlockWithLineBreak()
    {
        val inputPart = SingleBlock("class C\n{", "}", listOf(Whitespace("\n")))

        val expectedText = "class C\n" +
        "{\n" +
        "}"

        val actualText = MasterIndenter().indentPart(inputPart)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
