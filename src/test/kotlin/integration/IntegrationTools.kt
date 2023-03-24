package integration

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.MasterSplitter

class IntegrationTools
{
    companion object
    {
        fun test(inputText: String, expectedOutputText: String)
        {
            val parts = MasterSplitter().splitAll(inputText)
            //PartTools.printParts(parts)

            val actualOutputText = MasterIndenter(4).indentParts(parts)

            TestTools.assertAreEqual("Text", actualOutputText, expectedOutputText)
        }

        fun test2(inputText: String, expectedOutputText: String)
        {
            val parts1 = MasterSplitter().splitAll(inputText)
            val tempText = MasterIndenter(4).indentParts(parts1)

            val parts2 = MasterSplitter().splitAll(tempText)
            val actualOutputText = MasterIndenter(4).indentParts(parts2)

            TestTools.assertAreEqual("Text", actualOutputText, expectedOutputText)
        }
    }
}
