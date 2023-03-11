package tools

import TestParams
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class GetNonWhitespacePosAfterStartsWithTestsParametrizedWithWhitespaces(private val whitespace: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.whitespaces
    }

    @Test
    fun searchTextFoundAtTextStart2()
    {
        val inputText = "else${whitespace}def"

        val expectedPos = 4 + whitespace.length

        val actualPos = Tools.getElseEndPos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }

    @Test
    fun searchTextFoundAtTextStart3()
    {
        val inputText = "else${whitespace}${whitespace}def"

        val expectedPos = 4 + 2 * whitespace.length

        val actualPos = Tools.getElseEndPos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }

    @Test
    fun searchTextFoundAtTextEnd()
    {
        val inputText = "abc${whitespace}else"

        val expectedPos = -1

        val actualPos = Tools.getElseEndPos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }

    @Test
    fun searchTextFoundAtTextStartTODO()
    {
        val inputText = "${whitespace}else"

        val expectedPos = inputText.length

        val actualPos = Tools.getElseEndPos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }

    @Test
    fun searchTextFoundAtTextStartTODO2()
    {
        val inputText = "else${whitespace}"

        val expectedPos = inputText.length

        val actualPos = Tools.getElseEndPos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }

    @Test
    fun searchTextFoundAtTextStartTODO3()
    {
        val inputText = "${whitespace}else${whitespace}"

        val expectedPos = inputText.length

        val actualPos = Tools.getElseEndPos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }
}
