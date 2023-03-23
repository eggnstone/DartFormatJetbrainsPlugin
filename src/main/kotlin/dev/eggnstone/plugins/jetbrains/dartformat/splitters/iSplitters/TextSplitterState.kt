package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
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
        if (Constants.DEBUG) DotlinLogger.log("\n-----$s ---------------------------------------- $s")

        if (params != null)
        {
            if (Constants.DEBUG) DotlinLogger.log("params.expectClosingBrace: ${params.expectClosingBrace}")
            if (Constants.DEBUG) DotlinLogger.log("params.isEnum:             ${params.isEnum}")
        }

        if (Constants.DEBUG) DotlinLogger.log("currentText:               ${Tools.toDisplayString(currentText)}")
        if (Constants.DEBUG) DotlinLogger.log("remainingText:             ${Tools.toDisplayString(remainingText)}")

        if (Constants.DEBUG) DotlinLogger.log("currentBrackets:           ${Tools.toDisplayStringForStrings(currentBrackets)}")

        if (Constants.DEBUG) DotlinLogger.log("hasBlock:                  $hasBlock")
        if (Constants.DEBUG) DotlinLogger.log("hasColon:                  $hasColon")

        if (Constants.DEBUG) DotlinLogger.log("isInApostrophes:           $isInApostrophes")
        if (Constants.DEBUG) DotlinLogger.log("isInAssignment:            $isInAssignment")
        if (Constants.DEBUG) DotlinLogger.log("isInNormalQuotes:          $isInNormalQuotes")

        if (Constants.DEBUG) DotlinLogger.log("header:                    ${Tools.toDisplayString(header)}")
        if (Constants.DEBUG) DotlinLogger.log("middle:                    ${Tools.toDisplayString(middle)}")
        if (Constants.DEBUG) DotlinLogger.log("footer:                    ${Tools.toDisplayString(footer)}")

        if (Constants.DEBUG) DotlinLogger.log("blockParts:                ${Tools.toDisplayStringForParts(blockParts)}")

        if (Constants.DEBUG) DotlinLogger.log("----- $s ---------------------------------------- $s\n")
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
