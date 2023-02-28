package lineSplitter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.LineSplitter
import org.junit.Test

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        val expectedLines = listOf<String>()

        val actualLines = LineSplitter().split(inputText)

        TestTools.assertAreEqual(actualLines, expectedLines)
    }
}
