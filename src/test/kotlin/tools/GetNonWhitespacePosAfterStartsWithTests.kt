package tools

import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class GetNonWhitespacePosAfterStartsWithTests
{
    @Test
    fun searchTextNotFound()
    {
        val inputText = "abc"

        val expectedPos = -1

        val actualPos = Tools.getElseEndPos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }

    @Test
    fun searchTextNotFoundAtTextStart()
    {
        val inputText = "elsedef"

        val expectedPos = -1

        val actualPos = Tools.getElseEndPos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }

    @Test
    fun searchTextNotFoundAtTextEnd()
    {
        val inputText = "abcelse"

        val expectedPos = -1

        val actualPos = Tools.getElseEndPos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }

    @Test
    fun searchTextFoundAtTextStart()
    {
        val inputText = "else"

        val expectedPos = 4

        val actualPos = Tools.getElseEndPos(inputText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }
}
