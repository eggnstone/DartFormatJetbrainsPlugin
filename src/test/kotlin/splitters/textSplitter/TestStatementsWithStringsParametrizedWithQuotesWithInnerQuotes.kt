package splitters.textSplitter

import TestParams
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import splitters.SplitterTestTools

@RunWith(value = Parameterized::class)
class TestStatementsWithStringsParametrizedWithQuotesWithInnerQuotes(private val quote: String, private val innerQuote: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{2}")
        fun data() = TestParams.quotesWithInnerQuotes
    }

    @Test
    fun strings()
    {
        val inputText = "final String s = ${quote}Some text and then ${innerQuote}some inner Text$innerQuote and then the end.$quote;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun stringWithStatements()
    {
        val inputText = "final String s = ${quote}Some text abc(); and then ${innerQuote}some inner Text abc();$innerQuote and then the end.$quote;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun stringWithCurlyBrackets()
    {
        val inputText = "final String s = ${quote}Some text { brackets } and then ${innerQuote}some inner Text { brackets }$innerQuote and then the end.$quote;"

        val expectedRemainingText = ""
        val expectedPart = Statement(inputText)
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
