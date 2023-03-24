package splitters

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.ISplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.SplitParams

class SplitterTestTools
{
    companion object
    {
        fun testSplit(splitter: ISplitter, inputText: String, expectedRemainingText: String, expectedParts: List<IPart>)
        {
            testSplit(splitter, inputText, expectedRemainingText, expectedParts, SplitParams(), 0)
        }

        fun testSplit(splitter: ISplitter, inputText: String, expectedRemainingText: String, expectedParts: List<IPart>, params: SplitParams)
        {
            testSplit(splitter, inputText, expectedRemainingText, expectedParts, params, 0)
        }

        fun testSplit(splitter: ISplitter, inputText: String, expectedRemainingText: String, expectedParts: List<IPart>, currentIndent: Int)
        {
            testSplit(splitter, inputText, expectedRemainingText, expectedParts, SplitParams(), currentIndent)
        }

        fun testSplit(splitter: ISplitter, inputText: String, expectedRemainingText: String, expectedParts: List<IPart>, params: SplitParams, currentIndent: Int)
        {
            val actualResult = splitter.split(inputText, params, currentIndent)
            TestTools.assertPartsAreEqual("parts", actualResult.parts, expectedParts)
            TestTools.assertAreEqual("remainingText", actualResult.remainingText, expectedRemainingText, 4)
        }
    }
}
