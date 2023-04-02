package levels.levelsCalculator

import dev.eggnstone.plugins.jetbrains.dartformat.levels.BracketPackage
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSquareBrackets
{
    @Test
    fun commentWithClosingRoundBracketWithLineBreaks()
    {
        val inputText =
            "[\n" +
            "/*\n" +
            ")\n" +
            "*/\n" +
            "];"

        val expectedBracketPackagesSize = 1
        val expectedConditionals = 0

        val currentBracketPackages = listOf(BracketPackage(listOf(), 0))
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat("newBracketPackages.size", actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat("newConditionals", actualLevels.newConditionals, equalTo(expectedConditionals))
        MatcherAssert.assertThat("isInSquareBracketIf", actualLevels.isInSquareBracketIf, equalTo(false))
    }

    @Test
    fun commentWithClosingRoundBracketWithoutLineBreaks()
    {
        val inputText =
            "[\n" +
            "/* ) */\n" +
            "];"

        val expectedBracketPackagesSize = 1
        val expectedConditionals = 0

        val currentBracketPackages = listOf(BracketPackage(listOf("["), 0))
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat("newBracketPackages.size", actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat("newConditionals", actualLevels.newConditionals, equalTo(expectedConditionals))
        MatcherAssert.assertThat("isInSquareBracketIf", actualLevels.isInSquareBracketIf, equalTo(false))
    }
}
