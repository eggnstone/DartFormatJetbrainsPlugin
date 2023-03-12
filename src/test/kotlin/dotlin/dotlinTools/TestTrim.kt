package dotlin.dotlinTools

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import org.junit.Test

class TestTrim
{
    @Test
    fun trimStart()
    {
        val inputText = "  abc  "

        val expectedText = "abc  "

        val actualText = DotlinTools.trimStart(inputText)

        TestTools.assertAreEqual("Trimmed text", actualText, expectedText)
    }

    @Test
    fun trimStart2()
    {
        val inputText = "  "

        val expectedText = ""

        val actualText = DotlinTools.trimStart(inputText)

        TestTools.assertAreEqual("Trimmed text", actualText, expectedText)
    }

    @Test
    fun trimStartNothingToTrim()
    {
        val inputText = "abc  "

        val expectedText = "abc  "

        val actualText = DotlinTools.trimStart(inputText)

        TestTools.assertAreEqual("Trimmed text", actualText, expectedText)
    }

    @Test
    fun trimEnd()
    {
        val inputText = "  abc  "

        val expectedText = "  abc"

        val actualText = DotlinTools.trimEnd(inputText)

        TestTools.assertAreEqual("Trimmed text", actualText, expectedText)
    }

    @Test
    fun trimEnd2()
    {
        val inputText = "  "

        val expectedText = ""

        val actualText = DotlinTools.trimEnd(inputText)

        TestTools.assertAreEqual("Trimmed text", actualText, expectedText)
    }

    @Test
    fun trimEndNothingToTrim()
    {
        val inputText = "  abc"

        val expectedText = "  abc"

        val actualText = DotlinTools.trimEnd(inputText)

        TestTools.assertAreEqual("Trimmed text", actualText, expectedText)
    }

    @Test
    fun trim()
    {
        val inputText = "  abc  "

        val expectedText = "abc"

        val actualText = DotlinTools.trim(inputText)

        TestTools.assertAreEqual("Trimmed text", actualText, expectedText)
    }

    @Test
    fun trimNothingToTrim()
    {
        val inputText = "abc"

        val expectedText = "abc"

        val actualText = DotlinTools.trim(inputText)

        TestTools.assertAreEqual("Trimmed text", actualText, expectedText)
    }
}
