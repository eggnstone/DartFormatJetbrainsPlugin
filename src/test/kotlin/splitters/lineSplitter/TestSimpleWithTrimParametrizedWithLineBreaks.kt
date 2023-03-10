package splitters.lineSplitter

import TestParams
import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.LineSplitter
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestSimpleWithTrimParametrizedWithLineBreaks(private val lineBreak: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
    }

    @Test
    fun onlyLinebreak()
    {
        val inputText = lineBreak

        val expectedLines = listOf(lineBreak)

        val actualLines = LineSplitter().split(inputText, true)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }

    @Test
    fun textAndLineBreak()
    {
        val inputText = "a${lineBreak}"

        val expectedLines = listOf("a$lineBreak")

        val actualLines = LineSplitter().split(inputText, true)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }

    @Test
    fun textAndLineBreakAndText()
    {
        val inputText = "a${lineBreak}b"

        val expectedLines = listOf("a$lineBreak", "b")

        val actualLines = LineSplitter().split(inputText, true)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }

    @Test
    fun textAndLineBreaksAndText()
    {
        val inputText = "a${lineBreak}${lineBreak}b"

        val expectedLines = listOf("a$lineBreak", lineBreak, "b")

        val actualLines = LineSplitter().split(inputText, true)

        TestTools.assertStringsAreEqual(actualLines, expectedLines)
    }
}
