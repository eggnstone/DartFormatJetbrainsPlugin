package indenters.statement

import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{
    @Test
    fun simpleIf()
    {
        val inputPart = Statement("if\nabc();")

        val expectedText = "if\n" +
        "    abc();"

        val actualText = MasterIndenter().indentPart(inputPart)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
