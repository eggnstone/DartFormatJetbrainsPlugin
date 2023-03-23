package dotlin.dotlinTools

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TestContainsString
{
    @Test
    fun emptyTextInEmptyText()
    {
        val inputText = ""
        val inputSearchText = ""

        val expectedResult = true

        val actualResult = StringWrapper.containsString(inputText, inputSearchText)

        assertThat(actualResult, CoreMatchers.equalTo(expectedResult))
    }

    @Test
    fun emptyTextInNonEmptyText()
    {
        val inputText = "abc"
        val inputSearchText = ""

        val expectedResult = true

        val actualResult = StringWrapper.containsString(inputText, inputSearchText)

        assertThat(actualResult, CoreMatchers.equalTo(expectedResult))
    }

    @Test
    fun identicalTexts()
    {
        val inputText = "abc"
        val inputSearchText = "abc"

        val expectedResult = true

        val actualResult = StringWrapper.containsString(inputText, inputSearchText)

        assertThat(actualResult, CoreMatchers.equalTo(expectedResult))
    }

    @Test
    fun textNotFoundInLongerText()
    {
        val inputText = "abcdef"
        val inputSearchText = "xyz"

        val expectedResult = false

        val actualResult = StringWrapper.containsString(inputText, inputSearchText)

        assertThat(actualResult, CoreMatchers.equalTo(expectedResult))
    }

    @Test
    fun textNotFoundInShorterText()
    {
        val inputText = "a"
        val inputSearchText = "xyz"

        val expectedResult = false

        val actualResult = StringWrapper.containsString(inputText, inputSearchText)

        assertThat(actualResult, CoreMatchers.equalTo(expectedResult))
    }

    @Test
    fun textFoundAtBeginning()
    {
        val inputText = "abcdef"
        val inputSearchText = "abc"

        val expectedResult = true

        val actualResult = StringWrapper.containsString(inputText, inputSearchText)

        assertThat(actualResult, CoreMatchers.equalTo(expectedResult))
    }

    @Test
    fun textFoundAtEnd()
    {
        val inputText = "abcdef"
        val inputSearchText = "def"

        val expectedResult = true

        val actualResult = StringWrapper.containsString(inputText, inputSearchText)

        assertThat(actualResult, CoreMatchers.equalTo(expectedResult))
    }
}
