package indenters.master.getIndenter

import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.*
import dev.eggnstone.plugins.jetbrains.dartformat.parts.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{
    @Test
    fun whitespace()
    {
        val inputPart = Whitespace("")

        val indenter = MasterIndenter(4).getIndenter(inputPart)

        MatcherAssert.assertThat(indenter is WhitespaceIndenter, equalTo(true))
    }

    @Test
    fun statement()
    {
        val inputPart = Statement("")

        val indenter = MasterIndenter(4).getIndenter(inputPart)

        MatcherAssert.assertThat(indenter is StatementIndenter, equalTo(true))
    }

    @Test
    fun singleBlock()
    {
        val inputPart = MultiBlock.single("", "")

        val indenter = MasterIndenter(4).getIndenter(inputPart)

        MatcherAssert.assertThat(indenter is MultiBlockIndenter, equalTo(true))
    }

    @Test
    fun doubleBlock()
    {
        val inputPart = MultiBlock.double("", "", "")

        val indenter = MasterIndenter(4).getIndenter(inputPart)

        MatcherAssert.assertThat(indenter is MultiBlockIndenter, equalTo(true))
    }
}
