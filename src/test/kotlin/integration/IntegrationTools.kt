package integration

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.MasterSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.tools.SafetyTools

class IntegrationTools
{
    companion object
    {
        fun test(inputText: String, expectedOutputText: String, printParts: Boolean = false)
        {
            val parts = MasterSplitter().splitAll(inputText)

            if (printParts)
                PartTools.printParts(parts)

            val actualOutputText = MasterIndenter(4).indentParts(parts)

            TestTools.assertAreEqual("", actualOutputText, expectedOutputText)
            SafetyTools.checkForUnexpectedChanges(inputText, actualOutputText)
        }

        fun testTwice(inputText: String, expectedOutputText: String)
        {
            val parts1 = MasterSplitter().splitAll(inputText)
            val tempText = MasterIndenter(4).indentParts(parts1)

            TestTools.assertAreEqual("", tempText, expectedOutputText)
            SafetyTools.checkForUnexpectedChanges(inputText, tempText)

            val parts2 = MasterSplitter().splitAll(tempText)
            val actualOutputText = MasterIndenter(4).indentParts(parts2)

            TestTools.assertAreEqual("", actualOutputText, expectedOutputText)
            SafetyTools.checkForUnexpectedChanges(tempText, actualOutputText)
        }
    }
}
