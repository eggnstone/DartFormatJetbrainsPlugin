package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

class TextSplitterState(val inputText: String)
{
    var currentText = ""
    var remainingText = inputText

    var commentOnlyHashCode: Int? = null
    var currentBrackets = mutableListOf<String>()

    var hasBlock = false
    var hasColon = false

    var isInApostrophes = false
    var isInAssignment = false
    var isInNormalQuotes = false

    var headers = mutableListOf<String>()
    var parts = mutableListOf<List<IPart>>()
    var footer = ""

    var header = ""
    var middle = ""

    var blockParts: List<IPart> = listOf()

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

        DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(currentText)}")
        DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(remainingText)}")

        DotlinLogger.log("currentBrackets:           ${Tools.toDisplayStringForStrings(currentBrackets)}")

        DotlinLogger.log("hasBlock:                  $hasBlock")
        DotlinLogger.log("hasColon:                  $hasColon")

        DotlinLogger.log("isInApostrophes:           $isInApostrophes")
        DotlinLogger.log("isInAssignment:            $isInAssignment")
        DotlinLogger.log("isInNormalQuotes:          $isInNormalQuotes")

        DotlinLogger.log("header:                    ${Tools.toDisplayStringShort(header)}")
        DotlinLogger.log("middle:                    ${Tools.toDisplayStringShort(middle)}")

        DotlinLogger.log("headers:                   ${Tools.toDisplayStringForStrings(headers)}")
        DotlinLogger.log("parts:                     ${Tools.toDisplayStringForPartLists(parts)}")
        DotlinLogger.log("footer:                    ${Tools.toDisplayStringShort(footer)}")

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
