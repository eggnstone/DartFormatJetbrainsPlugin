package splitters.iSplitter.masterSplitter.getSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.MasterSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.WhitespaceSplitter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{
    @Test
    fun whitespace()
    {
        val inputText = " "

        val splitter = MasterSplitter().getSplitter(inputText)

        MatcherAssert.assertThat(splitter is WhitespaceSplitter, equalTo(true))
    }

    @Test
    fun nonWhitespace()
    {
        val inputText = ";"

        val splitter = MasterSplitter().getSplitter(inputText)

        MatcherAssert.assertThat(splitter is TextSplitter, equalTo(true))
    }

    @Test
    fun closingBrace()
    {
        val inputText = "}"

        val splitter = MasterSplitter().getSplitter(inputText)

        MatcherAssert.assertThat(splitter, equalTo(null))
    }
}
