package indenter.indent

import dev.eggnstone.plugins.jetbrains.dartformat.Indenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{
    @Test
    fun whitespace()
    {
        val inputParts = listOf<IPart>(Whitespace("\n\r\t "))

        val expectedText = ""

        val actualText = Indenter().indent(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }

    @Test
    fun simpleStatement()
    {
        val inputParts = listOf<IPart>(Statement("abc();"))

        val expectedText = "abc();"

        val actualText = Indenter().indent(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }

    @Test
    fun simpleIf()
    {
        val inputParts = listOf<IPart>(Statement("if\nabc();"))

        val expectedText = "if\n" +
        "    abc();"

        val actualText = Indenter().indent(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }

    @Test
    fun singleBlock()
    {
        val inputParts = listOf<IPart>(SingleBlock("class C\n{", "}"))

        val expectedText = "class C\n" +
        "{}"

        val actualText = Indenter().indent(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }

    @Test
    fun singleBlockWithLineBreak()
    {
        val inputParts = listOf<IPart>(SingleBlock("class C\n{", "}", listOf(Whitespace("\n"))))

        val expectedText = "class C\n" +
        "{\n" +
        "}"

        val actualText = Indenter().indent(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }

    @Test
    fun doubleBlock()
    {
        val inputParts = listOf<IPart>(
            DoubleBlock(
                "if (true)\n" +
                "{", "}\n" +
                "else\n" +
                "{", "}"
            )
        )

        val expectedText = "if (true)\n" +
        "{}\n" +
        "else\n" +
        "{}"

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
                listOf<IPart>(Whitespace("\n")),
                listOf<IPart>(Whitespace("\n"))
            )
        )

        val expectedText = "if (true)\n" +
        "{\n" +
        "}\n" +
        "else\n" +
        "{\n" +
        "}"

        val actualText = Indenter().indent(inputParts)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
