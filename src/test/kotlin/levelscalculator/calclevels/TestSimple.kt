package levelscalculator.calclevels

import dev.eggnstone.plugins.jetbrains.dartformat.LevelsCalculator
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestSimple
{
    @Test
    fun semicolon()
    {
        val inputText = ";"

        val expectedCurrentLevel = 0
        val expectedNextLevel = 0
        val expectedConditionals = 0

        val actualLevels = LevelsCalculator.calcLevels(inputText)

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

        val actualLevels = LevelsCalculator.calcLevels(inputText)

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

        val actualLevels = LevelsCalculator.calcLevels(inputText)

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

        val actualLevels = LevelsCalculator.calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }

    @Test
    fun blockStart()
    {
        val inputText = "{"

        val expectedCurrentLevel = 0
        val expectedNextLevel = 1
        val expectedConditionals = 0

        val actualLevels = LevelsCalculator.calcLevels(inputText)

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

        val actualLevels = LevelsCalculator.calcLevels(inputText)

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

        val actualLevels = LevelsCalculator.calcLevels(inputText)

        MatcherAssert.assertThat(actualLevels.currentLevel, equalTo(expectedCurrentLevel))
        MatcherAssert.assertThat(actualLevels.nextLevel, equalTo(expectedNextLevel))
        MatcherAssert.assertThat(actualLevels.conditionals, equalTo(expectedConditionals))
    }
}
