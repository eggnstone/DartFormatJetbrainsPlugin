package splitters.text

import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TextSplitter
import org.junit.Test
import splitters.SplitterTools

class TestDoubleBlocks
{
    @Test
    fun conditionalWithSimpleIfElse()
    {
        val inputText = "if (true) { statement1; } else { statement2; }"

        val expectedRemainingText = ""
        val parts1 = listOf(Whitespace(" "), Statement("statement1;"), Whitespace(" "))
        val parts2 = listOf(Whitespace(" "), Statement("statement2;"), Whitespace(" "))
        val expectedPart = DoubleBlock("if (true) {", "} else {", "}", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTools.test(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
