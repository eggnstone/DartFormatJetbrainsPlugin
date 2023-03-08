package splitters

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.ISplitter
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert

class SplitterTestTools
{
    companion object
    {
        fun testSplit(splitter: ISplitter, inputText: String, expectedRemainingText: String, expectedParts: List<IPart>)
        {
            val actualResult = splitter.split(inputText)
            //DotlinLogger.log("  parts:         ${Tools.toDisplayStringForParts(actualResult.parts)}")
            //DotlinLogger.log("  remainingText: ${Tools.toDisplayString(actualResult.remainingText)}")

            TestTools.assertAreEqual(Tools.toDisplayStringSimple(actualResult.remainingText), Tools.toDisplayStringSimple(expectedRemainingText))
            //TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)

            MatcherAssert.assertThat(actualResult.parts, CoreMatchers.equalTo(expectedParts))
        }
    }
}
