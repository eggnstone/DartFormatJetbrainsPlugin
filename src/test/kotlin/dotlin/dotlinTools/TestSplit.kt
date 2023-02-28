package dotlin.dotlinTools

import TestTools.Companion.assertAreEqual
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestSplit
{
    @Test
    fun emptyAll()
    {
        val inputText = ""
        val inputDelimiter = ""

        assertThrows<DartFormatException> { DotlinTools.split(inputText, inputDelimiter) }
    }

    @Test
    fun emptyText()
    {
        val inputText = ""
        val inputDelimiter = ","

        val expectedResult = listOf("")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun emptyText2()
    {
        val inputText = ""
        val inputDelimiter = ",-"

        val expectedResult = listOf("")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun emptyDelimiter()
    {
        val inputText = "a"
        val inputDelimiter = ""

        assertThrows<DartFormatException> { DotlinTools.split(inputText, inputDelimiter) }
    }

    @Test
    fun delimiterNotInText()
    {
        val inputText = "a"
        val inputDelimiter = ","

        val expectedResult = listOf("a")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterNotInText2()
    {
        val inputText = "a"
        val inputDelimiter = ",-"

        val expectedResult = listOf("a")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterNotInTextAB()
    {
        val inputText = "ab"
        val inputDelimiter = ","

        val expectedResult = listOf("ab")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterNotInTextAB2()
    {
        val inputText = "ab"
        val inputDelimiter = ",-"

        val expectedResult = listOf("ab")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun onlyDelimiter()
    {
        val inputText = ","
        val inputDelimiter = ","

        val expectedResult = listOf("")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun onlyDelimiter2()
    {
        val inputText = ",-"
        val inputDelimiter = ",-"

        val expectedResult = listOf("")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextStart()
    {
        val inputText = ",a"
        val inputDelimiter = ","

        val expectedResult = listOf("", "a")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextStart2()
    {
        val inputText = ",-a"
        val inputDelimiter = ",-"

        val expectedResult = listOf("", "a")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextEnd()
    {
        val inputText = "a,"
        val inputDelimiter = ","

        val expectedResult = listOf("a", "")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextEnd2()
    {
        val inputText = "a,-"
        val inputDelimiter = ",-"

        val expectedResult = listOf("a", "")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextMiddle()
    {
        val inputText = "a,b"
        val inputDelimiter = ","

        val expectedResult = listOf("a", "", "b")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun delimiterAtTextMiddle2()
    {
        val inputText = "a,-b"
        val inputDelimiter = ",-"

        val expectedResult = listOf("a", "", "b")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun twoSeparateDelimitersAtTextMiddle()
    {
        val inputText = "a,b,c"
        val inputDelimiter = ","

        val expectedResult = listOf("a", "", "b", "", "c")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun twoSeparateDelimitersAtTextMiddle2()
    {
        val inputText = "a,-b,-c"
        val inputDelimiter = ",-"

        val expectedResult = listOf("a", "", "b", "", "c")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun twoDelimitersAtTextMiddle()
    {
        val inputText = "a,,b"
        val inputDelimiter = ","

        val expectedResult = listOf("a", "", "", "b")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }

    @Test
    fun twoDelimitersAtTextMiddle2()
    {
        val inputText = "a,-,-b"
        val inputDelimiter = ",-"

        val expectedResult = listOf("a", "", "", "b")

        val actualResult = DotlinTools.split(inputText, inputDelimiter)

        assertAreEqual(actualResult, expectedResult)
    }
}
