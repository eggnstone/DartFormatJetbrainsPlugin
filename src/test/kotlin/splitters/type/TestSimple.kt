package splitters.type

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TypeSplitter
import org.junit.Test

class TestSimple
{
    @Test
    fun textWhitespaceText()
    {
        val inputText = "a  b"

        val expectedLines = listOf("a", "  ", "b")

        val actualLines = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }

    @Test
    fun whitespaceTextWhitespace()
    {
        val inputText = "  a  "

        val expectedLines = listOf("  ", "a", "  ")

        val actualLines = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }

    @Test
    fun spaceAndBrackets()
    {
        val inputText = "if (true)"

        val expectedParts = listOf("if", " ", "(", "true", ")")

        val actualParts = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualParts, expectedParts)
    }

    @Test
    fun brackets()
    {
        val inputText = "if(true)"

        val expectedParts = listOf("if", "(", "true", ")")

        val actualParts = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualParts, expectedParts)
    }
}
