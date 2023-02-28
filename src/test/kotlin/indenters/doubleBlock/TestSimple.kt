package indenters.doubleBlock

import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{

    @Test
    fun doubleBlock()
    {
        val inputPart =
            DoubleBlock(
                "if (true)\n" +
                "{", "}\n" +
                "else\n" +
                "{", "}"
            )

        val expectedText = "if (true)\n" +
        "{}\n" +
        "else\n" +
        "{}"

        val actualText = MasterIndenter().indentPart(inputPart)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }

    @Test
    fun doubleBlockWithLineBreaks()
    {
        val inputPart =
            DoubleBlock(
                "if (true)\n" +
                "{", "}\n" +
                "else\n" +
                "{", "}",
                listOf<IPart>(Whitespace("\n")),
                listOf<IPart>(Whitespace("\n"))
            )

        val expectedText = "if (true)\n" +
        "{\n" +
        "}\n" +
        "else\n" +
        "{\n" +
        "}"

        val actualText = MasterIndenter().indentPart(inputPart)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
