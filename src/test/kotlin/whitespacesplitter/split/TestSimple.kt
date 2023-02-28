package whitespacesplitter.split

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.WhitespaceSplitter
import org.junit.Test

class TestSimple
{
    @Test
    fun textWhitespaceText()
    {
        val inputText = "a  b"

        val expectedLines = listOf("a", "  ", "b")

        val actualLines = WhitespaceSplitter().split(inputText)

        TestTools.assertAreEqual(actualLines, expectedLines)
    }

    @Test
    fun whitespaceTextWhitespace()
    {
        val inputText = "  a  "

        val expectedLines = listOf("  ", "a", "  ")

        val actualLines = WhitespaceSplitter().split(inputText)

        TestTools.assertAreEqual(actualLines, expectedLines)
    }
}
