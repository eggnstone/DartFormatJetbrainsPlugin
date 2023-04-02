package levels.levelsCalculator

import dev.eggnstone.plugins.jetbrains.dartformat.levels.BracketPackage
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestStatementsWithComments
{
    @Test
    fun endOfLineCommentWithCommandAndClosingRoundBracket()
    {
        val inputText = "abc(); //,);"

        val expectedBracketPackagesSize = 0
        val expectedConditionals = 0

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat(actualLevels.newConditionals, equalTo(expectedConditionals))
    }

    @Test
    fun multiLineCommentWithCommandAndClosingRoundBracket()
    {
        val inputText = "abc(); /*,);*/"

        val expectedBracketPackagesSize = 0
        val expectedConditionals = 0

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.newBracketPackages.size, equalTo(expectedBracketPackagesSize))
        MatcherAssert.assertThat(actualLevels.newConditionals, equalTo(expectedConditionals))
    }
}
