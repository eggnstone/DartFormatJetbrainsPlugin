package splitters.text

import dev.eggnstone.plugins.jetbrains.dartformat.parts.*
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TextSplitter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

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

        val result = TextSplitter().split(inputText)
        //PartTools.printParts(expectedParts, "Expected")
        //PartTools.printParts(result.parts, "Actual  ")

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))

        PartTools.printParts(result.parts)
    }
}
