package levels.levelsCalculator

import TestParams
import dev.eggnstone.plugins.jetbrains.dartformat.levels.BracketPackage
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

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
    fun openingBraceInString()
    {
        val inputText = "abc($quote{$quote);"

        val expectedBracketPackagesSize = 0
        val expectedConditionals = 0

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat(actualLevels.newConditionals, equalTo(expectedConditionals))
    }

    @Test
    @Ignore
    fun escapedClosingQuoteAndOpeningBraceInString()
    {
        val inputText = "abc($quote\\$quote{$quote);"

        val expectedBracketPackagesSize = 0
        val expectedConditionals = 0

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat(actualLevels.newConditionals, equalTo(expectedConditionals))
    }

    @Test
    fun closingBraceInString()
    {
        val inputText = "abc($quote}$quote);"

        val expectedBracketPackagesSize = 0
        val expectedConditionals = 0

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat(actualLevels.newConditionals, equalTo(expectedConditionals))
    }

    @Test
    @Ignore
    fun escapedClosingQuoteAndClosingBraceInString()
    {
        val inputText = "abc($quote\\$quote}$quote);"

        val expectedBracketPackagesSize = 0
        val expectedConditionals = 0

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat(actualLevels.newConditionals, equalTo(expectedConditionals))
    }
}
