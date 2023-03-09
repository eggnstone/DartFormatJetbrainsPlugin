package levels.levelsCalculator

import dev.eggnstone.plugins.jetbrains.dartformat.levels.BracketPackage
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestBlocks
{
    @Test
    fun blockStart()
    {
        val inputText = "{"

        val expectedBracketPackagesSize = 1
        val expectedConditionals = 0

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat(actualLevels.newConditionals, equalTo(expectedConditionals))
    }

    @Test
    fun blockEnd()
    {
        val inputText = "}"

        val expectedBracketPackagesSize = 0
        val expectedConditionals = 0

        val currentBracketPackages = listOf(BracketPackage(listOf("{"), 1))
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat(actualLevels.newConditionals, equalTo(expectedConditionals))
    }

    @Test
    fun completeBlock()
    {
        val inputText = "{}"

        val expectedBracketPackagesSize = 0
        val expectedConditionals = 0

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat(actualLevels.newConditionals, equalTo(expectedConditionals))
    }
}
