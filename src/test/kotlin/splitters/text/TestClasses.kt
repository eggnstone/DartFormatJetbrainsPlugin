package splitters.text

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TextSplitter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestClasses
{
    @Test
    fun simpleClass()
    {
        val inputText = "class C {}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("class C {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = TextSplitter().split(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))

        PartTools.printParts(result.parts)
    }

    @Test
    fun simpleAbstractClass()
    {
        val inputText = "abstract class C {}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("abstract class C {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        val result = TextSplitter().split(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.parts, equalTo(expectedParts))

        PartTools.printParts(result.parts)
    }
}
