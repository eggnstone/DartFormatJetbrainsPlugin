package splitters.typeSplitter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TypeSplitter
import org.junit.Test

class TestCommas
{
    @Test
    fun comma()
    {
        val inputText = "a,b"

        val expectedLines = listOf("a", ",", "b")

        val actualLines = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }
}
