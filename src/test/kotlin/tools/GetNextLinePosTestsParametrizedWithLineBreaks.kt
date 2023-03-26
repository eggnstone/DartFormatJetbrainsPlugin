package tools

import TestParams
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class GetNextLinePosTestsParametrizedWithLineBreaks(private val lineBreak: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
    }

    @Test
    fun emptySecondLine()
    {
        val inputText = "abc$lineBreak"

        val expectedPos = 3 + lineBreak.length

        val actualPos = Tools.getNextLinePos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }

    @Test
    fun filledSecondLine()
    {
        val inputText = "abc${lineBreak}def"

        val expectedPos = 3 + lineBreak.length

        val actualPos = Tools.getNextLinePos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }
}
