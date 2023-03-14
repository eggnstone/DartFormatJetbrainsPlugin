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
    }
}
