package levelsCalculator

import dev.eggnstone.plugins.jetbrains.dartformat.LevelsCalculator
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        val expectedCurrentLevel = 0
        val expectedNextLevel = 0
        val expectedConditionals = 0

        val actualLevels = LevelsCalculator.calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }
}
