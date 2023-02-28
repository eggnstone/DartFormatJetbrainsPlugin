package indenter.indent

import dev.eggnstone.plugins.jetbrains.dartformat.Indenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestComplex
{
    @Test
    fun twoNestedIfs()
    {
        val inputParts = listOf<IPart>(Statement("if (true)\nif (true)\nabc();"))

        val expectedText = "if (true)\n" +
        "    if (true)\n" +
        "        abc();"

        val actualText = Indenter().indent(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }

    @Test
    fun threNestedIfs()
    {
        val inputParts = listOf<IPart>(Statement("if (true)\nif (true)\nif (true)\nabc();"))

        val expectedText = "if (true)\n" +
        "    if (true)\n" +
        "        if (true)\n" +
        "            abc();"

        val actualText = Indenter().indent(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }

    @Test
    fun doubleBlockWithLineBreaks()
    {
        val inputParts = listOf<IPart>(
            DoubleBlock(
                "if (true)\n" +
                "{", "}\n" +
                "else\n" +
                "{", "}",
                listOf(Whitespace("\n"), Statement("abc();"), Whitespace("\n")),
                listOf(Whitespace("\n"), Statement("def();"), Whitespace("\n"))
            )
        )

        val expectedText = "if (true)\n" +
        "{\n" +
        "    abc();\n" +
        "}\n" +
        "else\n" +
        "{\n" +
        "    def();\n" +
        "}"

        val actualText = Indenter().indent(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
