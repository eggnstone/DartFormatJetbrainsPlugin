package splitters.stringSplitter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.StringSplitter
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun emptyAll()
    {
        val inputText = ""
        val inputDelimiter = ""

        assertThrows<DartFormatException> { StringSplitter.split(inputText, inputDelimiter, trim = false) }
    }

    @Test
    fun emptyDelimiter()
    {
        val inputText = "a"
        val inputDelimiter = ""

        assertThrows<DartFormatException> { StringSplitter.split(inputText, inputDelimiter, trim = false) }
    }

    @Test
    fun emptyText()
    {
        val inputText = ""
        val inputDelimiter = ","

        val expectedResult = listOf("")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun emptyText2()
    {
        val inputText = ""
        val inputDelimiter = ",-"

        val expectedResult = listOf("")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }
}
