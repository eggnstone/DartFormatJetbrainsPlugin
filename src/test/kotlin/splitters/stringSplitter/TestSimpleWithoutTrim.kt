package splitters.stringSplitter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.StringSplitter
import org.junit.Test

class TestSimpleWithoutTrim
{
    @Test
    fun delimiterNotInText()
    {
        val inputText = "a"
        val inputDelimiter = ","

        val expectedResult = listOf("a")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterNotInText2()
    {
        val inputText = "a"
        val inputDelimiter = ",-"

        val expectedResult = listOf("a")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterNotInTextAB()
    {
        val inputText = "ab"
        val inputDelimiter = ","

        val expectedResult = listOf("ab")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterNotInTextAB2()
    {
        val inputText = "ab"
        val inputDelimiter = ",-"

        val expectedResult = listOf("ab")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun onlyDelimiter()
    {
        val inputText = ","
        val inputDelimiter = ","

        val expectedResult = listOf("")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun onlyDelimiter2()
    {
        val inputText = ",-"
        val inputDelimiter = ",-"

        val expectedResult = listOf("")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextStart()
    {
        val inputText = ",a"
        val inputDelimiter = ","

        val expectedResult = listOf("", "a")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextStart2()
    {
        val inputText = ",-a"
        val inputDelimiter = ",-"

        val expectedResult = listOf("", "a")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextEnd()
    {
        val inputText = "a,"
        val inputDelimiter = ","

        val expectedResult = listOf("a", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextEnd2()
    {
        val inputText = "a,-"
        val inputDelimiter = ",-"

        val expectedResult = listOf("a", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextMiddle()
    {
        val inputText = "a,b"
        val inputDelimiter = ","

        val expectedResult = listOf("a", "", "b")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextMiddle2()
    {
        val inputText = "a,-b"
        val inputDelimiter = ",-"

        val expectedResult = listOf("a", "", "b")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun twoSeparateDelimitersAtTextMiddle()
    {
        val inputText = "a,b,c"
        val inputDelimiter = ","

        val expectedResult = listOf("a", "", "b", "", "c")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun twoSeparateDelimitersAtTextMiddle2()
    {
        val inputText = "a,-b,-c"
        val inputDelimiter = ",-"

        val expectedResult = listOf("a", "", "b", "", "c")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun twoDelimitersAtTextMiddle()
    {
        val inputText = "a,,b"
        val inputDelimiter = ","

        val expectedResult = listOf("a", "", "", "b")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun twoDelimitersAtTextMiddle2()
    {
        val inputText = "a,-,-b"
        val inputDelimiter = ",-"

        val expectedResult = listOf("a", "", "", "b")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun onlySpacesAfterDelimiter()
    {
        val inputText = "abc,    "
        val inputDelimiter = ","

        val expectedResult = listOf("abc", "", "    ")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = false)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }
}
