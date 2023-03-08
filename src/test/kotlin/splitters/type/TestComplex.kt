package splitters.type

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
    fun multipleCurlyBrackets()
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
