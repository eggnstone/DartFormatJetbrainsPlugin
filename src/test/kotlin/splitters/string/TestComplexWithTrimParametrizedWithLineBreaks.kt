package splitters.string

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.StringSplitter
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestComplexWithTrimParametrizedWithLineBreaks(private val lineBreak: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
    }

    @Test
    fun trimStart()
    {
        val inputText = " a$lineBreak"
        val inputDelimiter = lineBreak

        val expectedResult = listOf("a", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = true)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun trimEnd()
    {
        val inputText = "a $lineBreak"
        val inputDelimiter = lineBreak

        val expectedResult = listOf("a", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = true)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun trimStartAndEnd()
    {
        val inputText = " a $lineBreak"
        val inputDelimiter = lineBreak

        val expectedResult = listOf("a", "")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = true)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }

    @Test
    fun trimStartAndEnd2()
    {
        val inputText = " a $lineBreak b "
        val inputDelimiter = lineBreak

        val expectedResult = listOf("a", "", "b")

        val actualResult = StringSplitter.split(inputText, inputDelimiter, trim = true)

        TestTools.assertStringsAreEqual(actualResult, expectedResult)
    }
}
