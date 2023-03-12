package splitters

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.ISplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.SplitParams

class SplitterTestTools
{
    companion object
    {
        fun testSplit(splitter: ISplitter, inputText: String, expectedRemainingText: String, expectedParts: List<IPart>, params: SplitParams = SplitParams())
        {
            val actualResult = splitter.split(inputText, params)
            //DotlinLogger.log("  parts:         ${Tools.toDisplayStringForParts(actualResult.parts)}")
            //DotlinLogger.log("  remainingText: ${Tools.toDisplayString(actualResult.remainingText)}")

            //DotlinLogger.log(      "XXX "+      Tools.toDisplayStringForParts(actualResult.parts))
            //DotlinLogger.log(      "YYY "+      Tools.toDisplayStringForParts(expectedParts))
            TestTools.assertPartsAreEqual("parts", actualResult.parts, expectedParts)
            //MatcherAssert.assertThat(actualResult.parts, CoreMatchers.equalTo(expectedParts))

            TestTools.assertAreEqual("remainingText", actualResult.remainingText, expectedRemainingText)
            //TestTools.assertAreEqual(actualResult.remainingText, expectedRemainingText)
        }
    }
}
