package indenters.multiBlockIndenter.indentHeader

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestIfElse
{
    @Test
    fun oneIf()
    {
        val inputText = "if (a)\n{"

        val expectedText = "if (a)\n{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun twoIfs()
    {
        val inputText = "if (a)\nif (b)\n{"

        val expectedText = "if (a)\n    if (b)\n    {"
        val expectedLevel = 1

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun threeIfs()
    {
        val inputText = "if (a)\nif (b)\nif (c)\n{"

        val expectedText = "if (a)\n    if (b)\n        if (c)\n        {"
        val expectedLevel = 2

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun doNotIndentBlockAfterElse()
    {
        val inputText = "}\nelse\n{"

        val expectedText = "}\nelse\n{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun indentAfterElse_doNotIndentBlockAfterElseIfElse()
    {
        val inputText = "}\nelse if (b)\nb();\nelse\n{"

        val expectedText = "}\nelse if (b)\n    b();\nelse\n{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }
}
