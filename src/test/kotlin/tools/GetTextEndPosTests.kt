package tools

import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class GetTextEndPosTests
{
    @Test
    fun textWithColon()
    {
        val inputText = "text:"
        val searchText = "text"

        val expectedPos = 4

        val actualPos = Tools.getTextEndPos(inputText, searchText)

        MatcherAssert.assertThat(actualPos, equalTo(expectedPos))
    }
}
