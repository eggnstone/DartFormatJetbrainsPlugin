package levelsCalculator

import dev.eggnstone.plugins.jetbrains.dartformat.LevelsCalculator
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestStatements
{
    @Test
    fun semicolon()
    {
        val inputText = ";"

        val expectedCurrentLevel = 0
        val expectedNextLevel = 0
        val expectedConditionals = 0

        val actualLevels = LevelsCalculator().calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }

    @Test
    fun statement()
    {
        val inputText = "abc();"

        val expectedCurrentLevel = 0
        val expectedNextLevel = 0
        val expectedConditionals = 0

        val actualLevels = LevelsCalculator().calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }

    @Test
    fun simpleIf()
    {
        val inputText = "if"

        val expectedCurrentLevel = 0
        val expectedNextLevel = 1
        val expectedConditionals = 1

        val actualLevels = LevelsCalculator().calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }

    @Test
    fun ifWithCondition()
    {
        val inputText = "if (true)"

        val expectedCurrentLevel = 0
        val expectedNextLevel = 1
        val expectedConditionals = 1

        val actualLevels = LevelsCalculator().calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }

    @Test
    fun twoIfsWithCondition()
    {
        val inputText = "if (true) if (true)"

        val expectedCurrentLevel = 0
        val expectedNextLevel = 2
        val expectedConditionals = 2

        val actualLevels = LevelsCalculator().calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }

    @Test
    fun threeIfsWithCondition()
    {
        val inputText = "if (true) if (true) if (true)"

        val expectedCurrentLevel = 0
        val expectedNextLevel = 3
        val expectedConditionals = 3

        val actualLevels = LevelsCalculator().calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }
}
