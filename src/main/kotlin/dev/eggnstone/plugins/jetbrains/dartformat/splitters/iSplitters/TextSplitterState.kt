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

    var hasBlockOLD = false
    var hasColon = false

    var isInApostrophes = false
    var isInAssignment = false
    var isInNormalQuotes = false

    var headers = mutableListOf<String>()
    var parts = mutableListOf<List<IPart>>()
    var footer = ""

    var headerOLD = ""
    var middleOLD = ""
    var blockPartsOLD: List<IPart> = listOf()

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

        DotlinLogger.log("hasBlock:                  $hasBlockOLD")
        DotlinLogger.log("hasColon:                  $hasColon")

        DotlinLogger.log("isInApostrophes:           $isInApostrophes")
        DotlinLogger.log("isInAssignment:            $isInAssignment")
        DotlinLogger.log("isInNormalQuotes:          $isInNormalQuotes")

        DotlinLogger.log("header:                    ${Tools.toDisplayStringShort(headerOLD)}")
        DotlinLogger.log("middle:                    ${Tools.toDisplayStringShort(middleOLD)}")
        DotlinLogger.log("blockParts:                ${Tools.toDisplayStringForParts(blockPartsOLD)}")

        DotlinLogger.log("headers:                   ${Tools.toDisplayStringForStrings(headers)}")
        DotlinLogger.log("parts:                     ${Tools.toDisplayStringForPartLists(parts)}")
        DotlinLogger.log("footer:                    ${Tools.toDisplayStringShort(footer)}")

        DotlinLogger.log("----- $s ---------------------------------------- $s\n")
    }

    fun clone(): TextSplitterState
    {
        val newState = TextSplitterState(inputText)

        newState.currentText = currentText
        newState.remainingText = remainingText

        newState.currentBrackets = DotlinTools.clone(currentBrackets)

        newState.hasBlockOLD = hasBlockOLD
        newState.hasColon = hasColon

        newState.isInApostrophes = isInApostrophes
        newState.isInAssignment = isInAssignment
        newState.isInNormalQuotes = isInNormalQuotes

        newState.headerOLD = headerOLD
        newState.middleOLD = middleOLD
        newState.blockPartsOLD = DotlinTools.clone(blockPartsOLD)

        newState.headers = DotlinTools.clone(headers)
        newState.parts = DotlinTools.clone(parts)
        newState.footer = footer


        return newState
    }
}
