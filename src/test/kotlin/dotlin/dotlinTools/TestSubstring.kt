package dotlin.dotlinTools

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestSubstring
{
    @Test
    fun startIndexBelow0()
    {
        val inputText = "abc"
        val startIndex = -1
        val endIndex = 1

        assertThrows<StringIndexOutOfBoundsException> { StringWrapper.substring(inputText, startIndex, endIndex) }
    }

    @Test
    fun endIndexAboveLengthPlus1()
    {
        val inputText = "abc"
        val startIndex = 0
        val endIndex = 10

        assertThrows<StringIndexOutOfBoundsException> { StringWrapper.substring(inputText, startIndex, endIndex) }
    }
}
