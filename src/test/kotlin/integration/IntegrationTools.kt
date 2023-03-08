package integration

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.MasterSplitter

class IntegrationTools
{
    companion object
    {
        fun test(inputText: String, expectedOutputText: String)
        {
            val parts = MasterSplitter().splitAll(inputText)
            PartTools.printParts(parts)

            val actualOutputText = MasterIndenter().indentParts(parts)

            TestTools.assertAreEqual(actualOutputText, expectedOutputText)
        }
    }
}
