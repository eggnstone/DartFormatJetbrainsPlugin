package linesplitter.split

import TestParams
import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.LineSplitter
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestSimple(private val lineBreak: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
    }

    @Test
    fun oneLineBreak()
    {
        val inputText = "a${lineBreak}b"

        val expectedLines = listOf("a$lineBreak", "b")

        val actualLines = LineSplitter().split(inputText)

        TestTools.assertAreEqual(actualLines, expectedLines)
    }
}
