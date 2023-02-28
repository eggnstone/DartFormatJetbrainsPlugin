package indenters.doubleBlock

import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestComplex
{
    @Test
    fun doubleBlockWithLineBreaks()
    {
        val inputPart =
            DoubleBlock(
                "if (true)\n" +
                "{", "}\n" +
                "else\n" +
                "{",
                "}",
                listOf(Whitespace("\n"), Statement("abc();"), Whitespace("\n")),
                listOf(Whitespace("\n"), Statement("def();"), Whitespace("\n"))
            )

        val expectedText = "if (true)\n" +
        "{\n" +
        "    abc();\n" +
        "}\n" +
        "else\n" +
        "{\n" +
        "    def();\n" +
        "}"

        val actualText = MasterIndenter().indentPart(inputPart)

        MatcherAssert.assertThat(actualText, equalTo(expectedText))
    }
}
