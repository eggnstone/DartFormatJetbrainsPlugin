package splitters.splitter.getSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.splitters.InstructionSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.Splitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.WhitespaceSplitter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{
    @Test
    fun whitespace()
    {
        val inputText = " "

        val splitter = Splitter().getSplitter(inputText)

        MatcherAssert.assertThat(splitter is WhitespaceSplitter, equalTo(true))
    }

    @Test
    fun nonWhitespace()
    {
        val inputText = ";"

        val splitter = Splitter().getSplitter(inputText)

        MatcherAssert.assertThat(splitter is InstructionSplitter, equalTo(true))
    }

    @Test
    fun closingCurlyBracket()
    {
        val inputText = "}"

        val splitter = Splitter().getSplitter(inputText)

        MatcherAssert.assertThat(splitter, equalTo(null))
    }
}
