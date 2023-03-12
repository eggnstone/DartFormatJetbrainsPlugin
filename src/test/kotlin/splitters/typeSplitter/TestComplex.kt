package splitters.typeSplitter

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TypeSplitter
import org.junit.Test

class TestComplex
{
    @Test
    fun multipleRoundBrackets()
    {
        val inputText = "if((true))"

        val expectedParts = listOf("if", "(", "(", "true", ")", ")")

        val actualParts = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualParts, expectedParts)
    }

    @Test
    fun multipleBraces()
    {
        val inputText = "if{{true}}"

        val expectedParts = listOf("if", "{", "{", "true", "}", "}")

        val actualParts = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualParts, expectedParts)
    }

    @Test
    fun mixedBrackets()
    {
        val inputText = "if(({{true}}))"

        val expectedParts = listOf("if", "(", "(", "{", "{", "true", "}", "}", ")", ")")

        val actualParts = TypeSplitter().split(inputText)

        TestTools.assertStringsAreEqual(actualParts, expectedParts)
    }
}
