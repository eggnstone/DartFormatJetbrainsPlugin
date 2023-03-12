package splitters.iSplitter.textSplitter

import TestParams
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import splitters.SplitterTestTools

@RunWith(value = Parameterized::class)
class TestStatementsWithStringsParametrizedWithQuotes(private val quote: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.quotes
    }

    @Test
    fun simpleStrings()
    {
        val inputText = "final String s = ${quote}Some text and then the end.$quote;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun stringWithStatement()
    {
        val inputText = "final String s = ${quote}Some text abc(); and then the end.$quote;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun stringWithBraces()
    {
        val inputText = "final String s = ${quote}Some text { brackets } and then the end.$quote;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
