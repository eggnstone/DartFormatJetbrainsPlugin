package splitters.line

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.LineSplitter
import org.junit.Test

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        val expectedLines = listOf<String>()

        val actualLines = LineSplitter().split(inputText, false)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }
}
