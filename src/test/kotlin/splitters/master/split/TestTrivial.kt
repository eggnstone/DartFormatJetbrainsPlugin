package splitters.master.split

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.MasterSplitter
import org.junit.Test
import splitters.SplitterTools

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        val expectedParts = listOf<IPart>()
        val expectedRemainingText = ""

        SplitterTools.test(MasterSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
