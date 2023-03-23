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

    var hasBlock = false
    var hasColon = false

    var isInApostrophes = false
    var isInAssignment = false
    var isInNormalQuotes = false

    var header = ""
    var middle = ""
    var footer = ""

    var blockParts: List<IPart> = listOf() // ok
    /*//var parts: List<X> = listOf() // ok
    var parts = listOf<X>() // error
    parts = mutableListOf<X>()*/

    fun log(s: String, params: SplitParams? = null)
    {
        if (!DotlinLogger.isEnabled)
            return

        DotlinLogger.log("\n-----$s ---------------------------------------- $s")

        if (params != null)
        {
            DotlinLogger.log("params.expectClosingBrace: ${params.expectClosingBrace}")
            DotlinLogger.log("params.isEnum:             ${params.isEnum}")
        }

        DotlinLogger.log("currentText:               ${Tools.toDisplayString(currentText)}")
        DotlinLogger.log("remainingText:             ${Tools.toDisplayString(remainingText)}")

        DotlinLogger.log("currentBrackets:           ${Tools.toDisplayStringForStrings(currentBrackets)}")

        DotlinLogger.log("hasBlock:                  $hasBlock")
        DotlinLogger.log("hasColon:                  $hasColon")

        DotlinLogger.log("isInApostrophes:           $isInApostrophes")
        DotlinLogger.log("isInAssignment:            $isInAssignment")
        DotlinLogger.log("isInNormalQuotes:          $isInNormalQuotes")

        DotlinLogger.log("header:                    ${Tools.toDisplayString(header)}")
        DotlinLogger.log("middle:                    ${Tools.toDisplayString(middle)}")
        DotlinLogger.log("footer:                    ${Tools.toDisplayString(footer)}")

        DotlinLogger.log("blockParts:                ${Tools.toDisplayStringForParts(blockParts)}")

        DotlinLogger.log("----- $s ---------------------------------------- $s\n")
    }

    fun clone(): TextSplitterState
    {
        val newState = TextSplitterState(inputText)

        newState.currentText = currentText
        newState.remainingText = remainingText

        newState.currentBrackets = DotlinTools.clone(currentBrackets)

        newState.hasBlock = hasBlock
        newState.hasColon = hasColon

        newState.isInApostrophes = isInApostrophes
        newState.isInAssignment = isInAssignment
        newState.isInNormalQuotes = isInNormalQuotes

        newState.header = header
        newState.middle = middle
        newState.footer = footer

        newState.blockParts = DotlinTools.clone(blockParts)

        return newState
    }
}
