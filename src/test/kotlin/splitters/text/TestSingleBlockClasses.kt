package splitters.text

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TextSplitter
import org.junit.Test
import splitters.SplitterTestTools

class TestSingleBlockClasses
{
    @Test
    fun simpleClass()
    {
        val inputText = "class C {}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("class C {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }

    @Test
    fun simpleAbstractClass()
    {
        val inputText = "abstract class C {}"

        val expectedRemainingText = ""
        val expectedPart = SingleBlock("abstract class C {", "}")
        val expectedParts = listOf<IPart>(expectedPart)

        SplitterTestTools.testSplit(TextSplitter(), inputText, expectedRemainingText, expectedParts)
    }
}
