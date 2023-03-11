package splitters.iSplitter.masterSplitter.split

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.MasterSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        val expectedParts = listOf<IPart>()
        val expectedRemainingText = ""

        SplitterTestTools.testSplit(MasterSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
