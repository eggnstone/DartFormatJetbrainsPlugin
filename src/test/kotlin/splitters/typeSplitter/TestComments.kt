package splitters.typeSplitter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TypeSplitter
import org.junit.Test

class TestComments
{
    @Test
    fun endOfLineComment()
    {
        val inputText = "a//bc"

        val expectedLines = listOf("a", "//", "bc")

        val actualLines = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }

    @Test
    fun endOfLineCommentWithAdditionalSlash()
    {
        val inputText = "a///bc"

        val expectedLines = listOf("a", "//", "/bc")

        val actualLines = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }

    @Test
    fun multiLineComment()
    {
        val inputText = "a/*bc*/"

        val expectedLines = listOf("a", "/*", "bc", "*/")

        val actualLines = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }
}
