package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class TextSplitterState(val inputText: String)
{
    var currentText = ""
    var remainingText = inputText

    var currentBrackets = mutableListOf<String>()

    var isDoubleBlock = false
    //var isFirstBlockWithBrackets = false
    //var isSecondBlockWithBrackets = false

    var isInApostrophes = false
    var isInAssignment = false
    var isInNormalQuotes = false

    var header = ""
    var middle = ""
    var footer = ""

    var parts1: List<IPart> = listOf() // ok
    /*//var parts: List<X> = listOf() // ok
    var parts = listOf<X>() // error
    parts = mutableListOf<X>()*/

    fun log(s: String)
    {
        DotlinLogger.log("--- $s ---")

        DotlinLogger.log("currentText:               ${Tools.toDisplayString(currentText)}")
        DotlinLogger.log("remainingText:             ${Tools.toDisplayString(remainingText)}")

        DotlinLogger.log("currentBrackets:           ${Tools.toDisplayStringForStrings(currentBrackets)}")

        DotlinLogger.log("isDoubleBlock:             $isDoubleBlock")
        //DotlinLogger.log("isFirstBlockWithBrackets:  $isFirstBlockWithBrackets")
        //DotlinLogger.log("isSecondBlockWithBrackets: $isSecondBlockWithBrackets")

        DotlinLogger.log("isInApostrophes:           $isInApostrophes")
        DotlinLogger.log("isInAssignment:            $isInAssignment")
        DotlinLogger.log("isInNormalQuotes:          $isInNormalQuotes")

        DotlinLogger.log("header:                    ${Tools.toDisplayString(header)}")
        DotlinLogger.log("middle:                    ${Tools.toDisplayString(middle)}")
        DotlinLogger.log("footer:                    ${Tools.toDisplayString(footer)}")

        DotlinLogger.log("parts1:                    ${Tools.toDisplayStringForParts(parts1)}")

        DotlinLogger.log("---")
    }

    fun clone(): TextSplitterState
    {
        val newState = TextSplitterState(inputText)

        newState.currentText = currentText
        newState.remainingText = remainingText

        newState.currentBrackets = DotlinTools.clone(currentBrackets)

        newState.isDoubleBlock = isDoubleBlock
        //newState.isFirstBlockWithBrackets = isFirstBlockWithBrackets
        //newState.isSecondBlockWithBrackets = isSecondBlockWithBrackets

        newState.isInApostrophes = isInApostrophes
        newState.isInAssignment = isInAssignment
        newState.isInNormalQuotes = isInNormalQuotes

        newState.header = header
        newState.middle = middle
        newState.footer = footer

        newState.parts1 = DotlinTools.clone(parts1)

        return newState
    }
}
