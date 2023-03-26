package tools

import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TrimSimpleTests
{
    @Test
    fun trimStartSimple_lineBreak()
    {
        val inputText = "\n"

        val expectedPos = "\n"

        val actualPos = Tools.trimStartSimple(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }

    @Test
    fun trimEndSimple_lineBreak()
    {
        val inputText = "\n"

        val expectedPos = "\n"

        val actualPos = Tools.trimEndSimple(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }
}
