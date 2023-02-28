package indenters.statement

import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestComplex
{
    @Test
    fun twoNestedIfs()
    {
        val inputPart = Statement("if (true)\nif (true)\nabc();")

        val expectedText = "if (true)\n" +
        "    if (true)\n" +
        "        abc();"

        val actualText = MasterIndenter().indentPart(inputPart)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }

    @Test
    fun threNestedIfs()
    {
        val inputPart = Statement("if (true)\nif (true)\nif (true)\nabc();")

        val expectedText = "if (true)\n" +
        "    if (true)\n" +
        "        if (true)\n" +
        "            abc();"

        val actualText = MasterIndenter().indentPart(inputPart)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
