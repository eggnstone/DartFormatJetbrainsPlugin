package whitespacesplitter.split

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.WhitespaceSplitter
import org.junit.Test

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        val expectedLines = listOf<String>()

        val actualLines = WhitespaceSplitter().split(inputText)

        TestTools.assertAreEqual(actualLines, expectedLines)
    }

    @Test
    fun whitespace()
    {
        val inputText = " "

        val expectedLines = listOf(" ")

        val actualLines = WhitespaceSplitter().split(inputText)

        TestTools.assertAreEqual(actualLines, expectedLines)
    }

    @Test
    fun text()
    {
        val inputText = "a"

        val expectedLines = listOf("a")

        val actualLines = WhitespaceSplitter().split(inputText)

        TestTools.assertAreEqual(actualLines, expectedLines)
    }
}
