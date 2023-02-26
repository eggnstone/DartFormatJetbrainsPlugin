package blockifiers.master.blockify

import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.MasterBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        val expectedParts = mutableListOf<IPart>()
        val expectedRemainingText = ""

        val actualResult = MasterBlockifier().blockify(inputText)

        MatcherAssert.assertThat(actualResult.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualResult.parts, equalTo(expectedParts))

        PartTools.printParts(actualResult.parts)
    }
}
