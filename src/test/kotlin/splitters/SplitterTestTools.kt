package splitters

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.ISplitter

class SplitterTestTools
{
    companion object
    {
        fun testSplit(splitter: ISplitter, inputText: String, expectedRemainingText: String, expectedParts: List<IPart>)
        {
            val actualResult = splitter.split(inputText)
            //DotlinLogger.log("  parts:         ${Tools.toDisplayStringForParts(actualResult.parts)}")
            //DotlinLogger.log("  remainingText: ${Tools.toDisplayString(actualResult.remainingText)}")

            TestTools.assertPartsAreEqual(actualResult.parts, expectedParts)
            //MatcherAssert.assertThat(actualResult.parts, CoreMatchers.equalTo(expectedParts))

            TestTools.assertAreEqual(Tools.toDisplayStringSimple(actualResult.remainingText), Tools.toDisplayStringSimple(expectedRemainingText))
            //TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
        }
    }
}
