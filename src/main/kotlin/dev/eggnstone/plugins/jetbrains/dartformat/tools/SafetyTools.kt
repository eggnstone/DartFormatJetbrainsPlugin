package dev.eggnstone.plugins.jetbrains.dartformat.tools

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import org.apache.commons.lang.StringUtils

class SafetyTools
{
    companion object
    {
        fun checkForUnexpectedChanges(oldText: String, newText: String)
        {
            val reducedOldText = oldText.replace(Regex("[\n\r\t ]"), "")
            val reducedNewText = newText.replace(Regex("[\n\r\t ]"), "")

            val commaLessOldText = reducedOldText.replace(",)", ")")
            val commaLessNewText = reducedNewText.replace(",)", ")")

            /*if (reducedNewText != commaLessOldText)
                TODO("missed some commas!")*/

            if (commaLessNewText == commaLessOldText)
                return

            val pos = StringUtils.indexOfDifference(commaLessOldText, commaLessNewText)
            throw DartFormatException("Unexpected difference in formatted text:" +
                " Old: ${Tools.toDisplayStringShort(StringWrapper.substring(commaLessOldText, pos))}" +
                " New: ${Tools.toDisplayStringShort(StringWrapper.substring(commaLessNewText, pos))}")
        }
    }
}
