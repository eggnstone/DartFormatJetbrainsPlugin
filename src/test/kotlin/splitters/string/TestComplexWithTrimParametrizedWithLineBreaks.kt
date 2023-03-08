package splitters.string

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.StringSplitter
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class TestComplexWithTrim
{
    @Test
    fun trimStart()
    {
        val inputText = " a\n"
        val inputDelimiter = "\n"

        val expectedResult = listOf("a", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = true)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun trimEnd()
    {
        val inputText = "a \n"
        val inputDelimiter = "\n"

        val expectedResult = listOf("a", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = true)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun trimStartAndEnd()
    {
        val inputText = " a \n"
        val inputDelimiter = "\n"

        val expectedResult = listOf("a", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = true)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun trimStartAndEnd2()
    {
        val inputText = " a \n b "
        val inputDelimiter = "\n"

        val expectedResult = listOf("a", "", "b")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = true)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }
}
