package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement

private data class HandleResult(val state: TextSplitterState, val splitResult: SplitResult?)

private class TextSplitterState(val inputText: String)
{
    var currentText = ""
    var remainingText = inputText

    var currentBrackets = mutableListOf<String>()

    var isDoubleBlock = false
    var isFirstBlockWithBrackets = false
    var isSecondBlockWithBrackets = false

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
        DotlinLogger.log("isFirstBlockWithBrackets:  $isFirstBlockWithBrackets")
        DotlinLogger.log("isSecondBlockWithBrackets: $isSecondBlockWithBrackets")

        DotlinLogger.log("isInApostrophes:           $isInApostrophes")
        DotlinLogger.log("isInAssignment:            $isInAssignment")
        DotlinLogger.log("isInNormalQuotes:          $isInNormalQuotes")

        DotlinLogger.log("header:                    ${Tools.toDisplayString(header)}")
        DotlinLogger.log("middle:                    ${Tools.toDisplayString(middle)}")
        DotlinLogger.log("footer:                    ${Tools.toDisplayString(footer)}")

        DotlinLogger.log("parts1:                    ${Tools.toDisplayStringForParts(parts1)}")

        DotlinLogger.log("")
    }

    fun clone(): TextSplitterState
    {
        val newState = TextSplitterState(inputText)

        newState.currentText = currentText
        newState.remainingText = remainingText

        newState.currentBrackets = DotlinTools.clone(currentBrackets)

        newState.isDoubleBlock = isDoubleBlock
        newState.isFirstBlockWithBrackets = isFirstBlockWithBrackets
        newState.isSecondBlockWithBrackets = isSecondBlockWithBrackets

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

class TextSplitter : ISplitter
{
    private var state = TextSplitterState("")

    override fun split(inputText: String): SplitResult
    {
        //DotlinLogger.log("TextSplitter.split: ${Tools.toDisplayString(Tools.shorten(inputText, 100, true))}")

        state = TextSplitterState(inputText)

        if (DotlinTools.isEmpty(inputText))
            throw DartFormatException("Unexpected empty text.")

        while (DotlinTools.isNotEmpty(state.remainingText))
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val currentChar = state.remainingText.get(0).toString() // workaround for dotlin for: for (c in text)

            if (state.isInNormalQuotes)
            {
                state = handleIsInNormalQuotes(currentChar, state)
                continue
            }

            if (state.isInApostrophes)
            {
                state = handleIsInApostrophes(currentChar, state)
                continue
            }

            if (currentChar == "\"")
            {
                state = handleNormalQuotes(currentChar, state)
                continue
            }

            if (currentChar == "'")
            {
                state = handleApostrophe(currentChar, state)
                continue
            }

            if (DotlinTools.startsWith(state.remainingText, "//") || DotlinTools.startsWith(state.remainingText, "/*"))
            {
                state = handleComment(state)
                continue
            }

            if (currentChar == "=" && DotlinTools.isEmpty(state.currentBrackets))
            {
                state = handleEqualSign(currentChar, state)
                continue
            }

            if (currentChar == ";" && DotlinTools.isEmpty(state.currentBrackets))
            {
                val handleResult = handleSemicolon(currentChar, state)
                if (handleResult.splitResult != null)
                    return handleResult.splitResult

                state = handleResult.state
                continue
            }

            if (currentChar == "{" && !state.isInAssignment && DotlinTools.isEmpty(state.currentBrackets))
            {
                val handleResult = handleOpeningCurlyBracket(currentChar, state)
                if (handleResult.splitResult != null)
                    return handleResult.splitResult

                state = handleResult.state
                continue
            }

            if (Tools.isOpeningBracket(currentChar))
            {
                state = handleOpeningBracket(currentChar, state)
                continue
            }

            if (Tools.isClosingBracket(currentChar))
            {
                state = handleClosingBracket(currentChar, state)
                continue
            }

            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)
        }

        state.log("7")
        throw DartFormatException("Unexpected end of block or statement.")
    }

    companion object
    {
        private fun handleIsInApostrophes(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            if (DotlinTools.startsWith(state.remainingText, "\\'"))
            {
                state.currentText += "\\'"
                state.remainingText = DotlinTools.substring(state.remainingText, 2)
                return state
            }

            if (DotlinTools.startsWith(state.remainingText, "'"))
                state.isInApostrophes = false

            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            return state
        }

        private fun handleIsInNormalQuotes(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            if (DotlinTools.startsWith(state.remainingText, "\\\""))
            {
                state.currentText += "\\\""
                state.remainingText = DotlinTools.substring(state.remainingText, 2)
                return state
            }

            if (DotlinTools.startsWith(state.remainingText, "\""))
                state.isInNormalQuotes = false

            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            return state
        }

        private fun handleApostrophe(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.isInApostrophes = true
            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            return state
        }

        private fun handleClosingBracket(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            if (DotlinTools.isEmpty(state.currentBrackets))
            {
                state.log("6")
                throw DartFormatException("Unexpected closing bracket: currentChar=${Tools.toDisplayString(currentChar)} remainingText=${Tools.toDisplayString(state.remainingText)}")
            }

            val lastOpeningBracket = state.currentBrackets.removeLast()
            val expectedClosingBracket = Tools.getClosingBracket(lastOpeningBracket)
            if (currentChar != expectedClosingBracket)
                TODO("handleClosingBracket: ${Tools.toDisplayString(state.remainingText)}")

            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            return state
        }

        private fun handleComment(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            val extractionResult = CommentExtractor.extract(state.remainingText)
            state.currentText += extractionResult.comment
            state.remainingText = extractionResult.remainingText

            return state
        }

        private fun handleEqualSign(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.isInAssignment = true
            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            return state
        }

        private fun handleNormalQuotes(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.isInNormalQuotes = true
            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            return state
        }

        private fun handleOpeningBracket(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.currentBrackets.add(currentChar)
            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            return state
        }

        private fun handleOpeningCurlyBracket(currentChar: String, oldState: TextSplitterState): HandleResult
        {
            val state = oldState.clone()

            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            val result = MasterSplitter().split(state.remainingText)
            state.remainingText = result.remainingText

            if (!DotlinTools.startsWith(state.remainingText, "}"))
            {
                state.log("4")
                TODO("error 1") // ${Tools.toDisplayString(currentText)} ${Tools.toDisplayString(header)} ${Tools.toDisplayString(remainingText)}")
            }

            if (state.isDoubleBlock)
            {
                if (state.remainingText == "}")
                    return HandleResult(state, SplitResult("", listOf(DoubleBlock(state.header, state.currentText, "}", state.parts1, result.parts))))

                state.log("5")
                TODO("is double block")
            }

            if (state.remainingText == "}")
            {
                //DotlinLogger.log("- Returning SingleBlock (remainingText == \"}\")")
                return HandleResult(state, SplitResult("", listOf(SingleBlock(state.currentText, "}", result.parts))))
            }

            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            val elseEndPos = Tools.getElseEndPos(state.remainingText)
            DotlinLogger.log("elseEndPos:                             $elseEndPos")

            if (elseEndPos == -1)
                return HandleResult(state, SplitResult(state.remainingText, listOf(SingleBlock(state.currentText, "}", result.parts))))

            DotlinLogger.log("remainingText:                          ${Tools.toDisplayString(state.remainingText)}")
            DotlinLogger.log("remainingText.substring(0, elseEndPos): ${Tools.toDisplayString(DotlinTools.substring(state.remainingText, 0, elseEndPos))}")

            val rest = DotlinTools.substring(state.remainingText, elseEndPos)
            DotlinLogger.log("rest:                                   ${Tools.toDisplayString(rest)}")

            if (DotlinTools.isEmpty(rest))
                throw DartFormatException("DotlinTools.isEmpty(trimmedRemainingTextAfterElse)")

            state.isDoubleBlock = true
            state.header = state.currentText
            state.parts1 = result.parts
            state.currentText = "}"

            return HandleResult(state, null)
        }

        private fun handleSemicolon(currentChar: String, oldState: TextSplitterState): HandleResult
        {
            val state = oldState.clone()

            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1) // removing the ";"
            state.log("1")

            if (!state.isDoubleBlock)
            {
                if (!DotlinTools.startsWith(state.remainingText, "}"))
                {
                    val elseEndPos = Tools.getElseEndPos(state.remainingText)
                    DotlinLogger.log("elseEndPos:                             $elseEndPos")

                    if (elseEndPos == -1)
                        return HandleResult(state, SplitResult(state.remainingText, listOf(Statement(state.currentText))))


                    state.isDoubleBlock = true
                    state.isFirstBlockWithBrackets = false
                    state.middle = DotlinTools.substring(state.remainingText, 0, elseEndPos)
                    state.remainingText = DotlinTools.substring(state.remainingText, elseEndPos) // removing the ";"
                    DotlinLogger.log("remainingText:                          ${Tools.toDisplayString(state.remainingText)}")
                    state.isSecondBlockWithBrackets = DotlinTools.startsWith(state.remainingText, "{")
                    return HandleResult(state, null)
                }
            }

            state.footer = ""
            if (DotlinTools.startsWith(state.currentText, "}"))
            {
                if (!state.isSecondBlockWithBrackets)
                    TODO()

                state.footer = "}"
                state.currentText = DotlinTools.substring(state.currentText, 1) // removing the "}"
            }
            else
            {
                if (state.isSecondBlockWithBrackets)
                    TODO()
            }

            state.log("2")

            val elseEndPos = Tools.getElseEndPos(state.currentText)
            DotlinLogger.log("elseEndPos:                $elseEndPos")

            if (elseEndPos == -1)
                TODO("elseEndPos == -1")

            state.log("3")

            state.middle += DotlinTools.substring(state.currentText, 0, elseEndPos)
            DotlinLogger.log("middle:                               ${Tools.toDisplayString(state.middle)}")

            val statement = DotlinTools.substring(state.currentText, elseEndPos)
            DotlinLogger.log("statement:                            ${Tools.toDisplayString(statement)}")

            val parts2 = listOf(Statement(statement))

            return HandleResult(state, SplitResult("", listOf(DoubleBlock(state.header, state.middle, "", state.parts1, parts2))))
        }
    }
}
