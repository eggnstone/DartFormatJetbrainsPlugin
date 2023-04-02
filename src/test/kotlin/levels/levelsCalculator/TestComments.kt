package levels.levelsCalculator

import dev.eggnstone.plugins.jetbrains.dartformat.levels.BracketPackage
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestComments
{
    @Test
    fun completeMultiLineComment()
    {
        val inputText = "a/*b*/c"

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.isInMultiLineComment, equalTo(false))
    }

    @Test
    fun incompleteMultiLineComment()
    {
        val inputText = "a/*b"

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages)

        MatcherAssert.assertThat(actualLevels.isInMultiLineComment, equalTo(true))
    }

    @Test
    fun x()
    {
        val inputText = ")*/"

        val currentBracketPackages = listOf<BracketPackage>()
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages, wasInMultiLineComment = true)

        MatcherAssert.assertThat(actualLevels.isInMultiLineComment, equalTo(false))
    }

    @Test
    fun x2()
    {
        val inputText = ")*/"

        val currentBracketPackages = listOf<BracketPackage>(BracketPackage(listOf("["), 0))
        val actualLevels = LevelsCalculator().calcLevels(inputText, 100, currentBracketPackages, wasInMultiLineComment = true)

        MatcherAssert.assertThat(actualLevels.isInMultiLineComment, equalTo(false))
    }
}
