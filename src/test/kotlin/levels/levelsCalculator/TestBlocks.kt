package levels.levelsCalculator

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.levels.BracketPackage
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

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
    fun unexpectedBlockEnd_Throws()
    {
        val inputText = "}"

        val currentBracketPackages = listOf<BracketPackage>()
        assertThrows<DartFormatException> { LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages) }
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
