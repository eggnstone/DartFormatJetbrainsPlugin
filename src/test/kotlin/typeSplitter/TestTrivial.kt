package typeSplitter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.TypeSplitter
import org.junit.Test

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        val expectedLines = listOf<String>()

        val actualLines = TypeSplitter().split(inputText)

        TestTools.assertAreEqual(actualLines, expectedLines)
    }

    @Test
    fun whitespace()
    {
        val inputText = " "

        val expectedLines = listOf(" ")

        val actualLines = TypeSplitter().split(inputText)

        TestTools.assertAreEqual(actualLines, expectedLines)
    }

    @Test
    fun text()
    {
        val inputText = "a"

        val expectedLines = listOf("a")

        val actualLines = TypeSplitter().split(inputText)

        TestTools.assertAreEqual(actualLines, expectedLines)
    }
}
