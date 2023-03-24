package dotlin.dotlinTools

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import org.junit.Test

class TestTrim
{
    @Test
    fun trimStart()
    {
        val inputText = "  abc  "

        val expectedText = "abc  "

        val actualText = StringWrapper.trimStart(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun trimStart2()
    {
        val inputText = "  "

        val expectedText = ""

        val actualText = StringWrapper.trimStart(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun trimStartNothingToTrim()
    {
        val inputText = "abc  "

        val expectedText = "abc  "

        val actualText = StringWrapper.trimStart(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun trimEnd()
    {
        val inputText = "  abc  "

        val expectedText = "  abc"

        val actualText = StringWrapper.trimEnd(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun trimEnd2()
    {
        val inputText = "  "

        val expectedText = ""

        val actualText = StringWrapper.trimEnd(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun trimEndNothingToTrim()
    {
        val inputText = "  abc"

        val expectedText = "  abc"

        val actualText = StringWrapper.trimEnd(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun trim()
    {
        val inputText = "  abc  "

        val expectedText = "abc"

        val actualText = StringWrapper.trim(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }

    @Test
    fun trimNothingToTrim()
    {
        val inputText = "abc"

        val expectedText = "abc"

        val actualText = StringWrapper.trim(inputText)

        TestTools.assertAreEqual("", actualText, expectedText)
    }
}
