package levelsCalculator

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

        val expectedCurrentLevel = 0
        val expectedNextLevel = 1
        val expectedConditionals = 0

        val actualLevels = LevelsCalculator().calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }

    @Test
    fun blockEnd()
    {
        val inputText = "}"

        val expectedCurrentLevel = -1
        val expectedNextLevel = -1
        val expectedConditionals = 0

        val actualLevels = LevelsCalculator().calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }

    @Test
    fun completeBlock()
    {
        val inputText = "{}"

        val expectedCurrentLevel = 0
        val expectedNextLevel = 0
        val expectedConditionals = 0

        val actualLevels = LevelsCalculator().calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }
}
