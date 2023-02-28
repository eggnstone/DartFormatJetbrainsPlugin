package indenters.master.getIndenter

import dev.eggnstone.plugins.jetbrains.dartformat.indenters.*
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{
    @Test
    fun whitespace()
    {
        val inputPart = Whitespace("")

        val indenter = MasterIndenter().getIndenter(inputPart)

        MatcherAssert.assertThat(indenter is WhitespaceIndenter, equalTo(true))
    }

    @Test
    fun statement()
    {
        val inputPart = Statement("")

        val indenter = MasterIndenter().getIndenter(inputPart)

        MatcherAssert.assertThat(indenter is StatementIndenter, equalTo(true))
    }

    @Test
    fun singleBlock()
    {
        val inputPart = SingleBlock("", "")

        val indenter = MasterIndenter().getIndenter(inputPart)

        MatcherAssert.assertThat(indenter is SingleBlockIndenter, equalTo(true))
    }

    @Test
    fun doubleBlock()
    {
        val inputPart = DoubleBlock("", "", "")

        val indenter = MasterIndenter().getIndenter(inputPart)

        MatcherAssert.assertThat(indenter is DoubleBlockIndenter, equalTo(true))
    }
}
