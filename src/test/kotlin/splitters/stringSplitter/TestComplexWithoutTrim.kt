package splitters.stringSplitter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.StringSplitter
import org.junit.Test

class TestComplexWithoutTrim
{
    @Test
    fun trimStart()
    {
        val inputText = " a\n"
        val inputDelimiter = "\n"

        val expectedResult = listOf(" a", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun trimEnd()
    {
        val inputText = "a \n"
        val inputDelimiter = "\n"

        val expectedResult = listOf("a ", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun trimStartAndEnd()
    {
        val inputText = " a \n"
        val inputDelimiter = "\n"

        val expectedResult = listOf(" a ", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun trimStartAndEnd2()
    {
        val inputText = " a \n b "
        val inputDelimiter = "\n"

        val expectedResult = listOf(" a ", "", " b ")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }
}
