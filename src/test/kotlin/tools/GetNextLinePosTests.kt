package tools

import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class GetNextLinePosTests
{
    @Test
    fun noNextLine()
    {
        val inputText = "abc"

        val expectedPos = -1

        val actualPos = Tools.getNextLinePos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }
}
