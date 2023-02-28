package splitters

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.ISplitter
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert

class SplitterTools
{
    companion object
    {
        fun test(splitter: ISplitter, inputText: String, expectedRemainingText: String, expectedParts: List<IPart>)
        {
            val actualResult = splitter.split(inputText)

            MatcherAssert.assertThat(actualResult.remainingText, CoreMatchers.equalTo(expectedRemainingText))
            MatcherAssert.assertThat(actualResult.parts, CoreMatchers.equalTo(expectedParts))
        }
    }
}
