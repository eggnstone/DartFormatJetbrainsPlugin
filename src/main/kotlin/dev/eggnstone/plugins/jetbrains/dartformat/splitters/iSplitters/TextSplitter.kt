package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement

class TextSplitter : ISplitter
{
    override val name = "Text"

    override fun split(inputText: String, params: SplitParams): SplitResult
    {
        DotlinLogger.log("TextSplitter.split: isEnum=${params.isEnum} ${Tools.toDisplayString(Tools.shorten(inputText, 100, true))}")

        if (DotlinTools.isEmpty(inputText))
            throw DartFormatException("Unexpected empty text.")

        //var expectClosingBrace = false
        var state = TextSplitterState(inputText)

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
                state = handleNormalQuotes(state)
                continue
            }

            if (currentChar == "'")
            {
                state = handleApostrophe(state)
                continue
            }

            if (DotlinTools.startsWith(state.remainingText, "//"))
            {
                state = handleEndOfLineComment(state)
                continue
                /*val handleResult = handleEndOfLineComment(state)
                if (handleResult.splitResult != null)
                    return handleResult.splitResult

                state = handleResult.state
                continue*/
            }

            if (DotlinTools.startsWith(state.remainingText, "/*"))
            {
                state = handleMultiLineComment(state)
                continue
            }

            if (currentChar == "=" && DotlinTools.isEmpty(state.currentBrackets))
            {
                state = handleEqualSign(state)
                continue
            }

            if (currentChar == ";" && DotlinTools.isEmpty(state.currentBrackets))
            {
                val handleResult = handleSemicolon(state)
                if (handleResult.splitResult != null)
                    return handleResult.splitResult

                state = handleResult.state
                continue
            }

            if (currentChar == "{" && !state.isInAssignment && DotlinTools.isEmpty(state.currentBrackets))
            {
                val handleResult = handleOpeningBrace(state)
                if (handleResult.splitResult != null)
                    return handleResult.splitResult

                //expectClosingBrace = true
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
                val handleResult = handleClosingBracket(currentChar, state, params)
                if (handleResult.splitResult != null)
                    return handleResult.splitResult

                state = handleResult.state
                continue
            }

            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)
        }

        state.log("TextSplitter.split error")
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

        private fun handleApostrophe(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.isInApostrophes = true
            state.currentText += "'"
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            return state
        }

        private fun handleClosingBracket(currentChar: String, oldState: TextSplitterState, params: SplitParams): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleClosingBracket", params)

            if (DotlinTools.isEmpty(state.currentBrackets))
            {
                if (params.isEnum)
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(Statement(state.currentText))))

                if (!params.expectClosingBrace)
                    throw DartFormatException("TextSplitter.handleClosingBracket: Unexpected closing bracket: currentChar=${Tools.toDisplayString(currentChar)} remainingText=${Tools.toDisplayString(state.remainingText)}")

                if (DotlinTools.isEmpty(state.currentText))
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf()))

                val statement = Statement(state.currentText)
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(statement)))
            }

            val lastOpeningBracket = state.currentBrackets.removeLast()
            val expectedClosingBracket = Tools.getClosingBracket(lastOpeningBracket)
            if (currentChar != expectedClosingBracket)
                TODO("handleClosingBracket: ${Tools.toDisplayString(state.remainingText)}")

            state.currentText += currentChar
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            return TextSplitterHandleResult(state, null)
        }

        private fun handleEndOfLineComment(oldState: TextSplitterState): TextSplitterState//TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleEndOfLineComment")

            DotlinLogger.log("Calling CommentExtractor ..")
            val extractionResult = CommentExtractor.extract(state.remainingText)
            DotlinLogger.log("Result from CommentExtractor:")
            DotlinLogger.log("  comment        ${Tools.toDisplayString(extractionResult.comment)}")
            DotlinLogger.log("  remainingText: ${Tools.toDisplayString(extractionResult.remainingText)}")

            state.currentText += extractionResult.comment
            DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = extractionResult.remainingText
            DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            return state
        }

        private fun handleMultiLineComment(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            val extractionResult = CommentExtractor.extract(state.remainingText)
            state.currentText += extractionResult.comment
            state.remainingText = extractionResult.remainingText

            return state
        }

        private fun handleEqualSign(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.isInAssignment = true
            state.currentText += "="
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            return state
        }

        private fun handleNormalQuotes(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.isInNormalQuotes = true
            state.currentText += "\""
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

        fun handleOpeningBrace(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleOpeningBrace")

            state.currentText += "{"
            DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = DotlinTools.substring(state.remainingText, 1)
            DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            val params = SplitParams(isEnum = DotlinTools.startsWith(state.currentText, "enum "), expectClosingBrace = true)
            DotlinLogger.log("params.isEnum:             ${params.isEnum}")

            DotlinLogger.log("-> MasterSplitter.split(   ${Tools.toDisplayString(state.remainingText)})")
            val result = MasterSplitter().split(state.remainingText, params)
            DotlinLogger.log("<- MasterSplitter.split(   ${Tools.toDisplayString(state.remainingText)})")
            DotlinLogger.log("  remainingText:           ${Tools.toDisplayString(result.remainingText)}")
            DotlinLogger.log("  parts:                   ${Tools.toDisplayStringForParts(result.parts)}")
            state.remainingText = result.remainingText

            if (!DotlinTools.startsWith(state.remainingText, "}"))
            {
                oldState.log("handleOpeningBrace - Missing closing brace (old)")
                state.log("handleOpeningBrace - Missing closing brace (new)")
                DotlinLogger.log("result.remainingText: ${Tools.toDisplayString(result.remainingText)}")
                DotlinLogger.log("result.parts: ${Tools.toDisplayStringForParts(result.parts)}")
                throw DartFormatException("Missing closing brace: ${Tools.toDisplayString(state.remainingText)}")
            }

            if (state.hasBlock)
            {
                if (state.remainingText == "}")
                {
                    state.remainingText = ""
                    state.middle += "{"
                    state.footer = "}"

                    state.log("handleOpeningBrace exit-1-DoubleBlock")
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(DoubleBlock(state.header, state.middle, state.footer, state.blockParts, result.parts))))
                }

                // starts with "}" but has more

                state.remainingText = DotlinTools.substring(state.remainingText, 1) // removing the "}"
                state.middle += "{"
                state.footer = "}"

                state.log("handleOpeningBrace exit-2-DoubleBlock")
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(DoubleBlock(state.header, state.middle, state.footer, state.blockParts, result.parts))))
            }

            if (state.remainingText == "}")
            {
                //DotlinLogger.log("- Returning SingleBlock (remainingText == \"}\")")
                state.log("handleOpeningBrace exit-2-SingleBlock")
                return TextSplitterHandleResult(state, SplitResult("", listOf(SingleBlock(state.currentText, "}", result.parts))))
            }

            state.remainingText = DotlinTools.substring(state.remainingText, 1) // removing the "}"
            DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            val elseEndPos = Tools.getElseEndPos(state.remainingText)
            DotlinLogger.log("elseEndPos:                $elseEndPos")

            if (elseEndPos == -1)
            {
                state.footer = "}"
                state.log("handleOpeningBrace exit-3-SingleBlock")
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(SingleBlock(state.currentText, state.footer, result.parts))))
            }

            state.middle = "}" + DotlinTools.substring(state.remainingText, 0, elseEndPos)
            DotlinLogger.log("middle:                    ${Tools.toDisplayString(state.middle)}")

            state.remainingText = DotlinTools.substring(state.remainingText, elseEndPos)
            DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            if (DotlinTools.isEmpty(state.remainingText))
                throw DartFormatException("TODO")

            state.hasBlock = true
            state.header = state.currentText
            state.blockParts = result.parts
            state.currentText = ""

            state.log("handleOpeningBrace exit-4")
            return TextSplitterHandleResult(state, null)
        }

        fun handleSemicolon(oldState: TextSplitterState): TextSplitterHandleResult
        {
            if (oldState.hasBlock)
                return handleSemicolonHasBlock(oldState)

            return handleSemicolonHasNoBlock(oldState)
        }

        fun handleSemicolonHasBlock(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasBlock")

            state.currentText += ";"
            DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = DotlinTools.substring(state.remainingText, 1) // removing the ";"
            DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            state.footer = state.middle + state.currentText
            DotlinLogger.log("footer:                   ${Tools.toDisplayString(state.footer)}")
            state.middle = ""
            DotlinLogger.log("middle:                   ${Tools.toDisplayString(state.middle)}")
            state.currentText = ""
            DotlinLogger.log("currentText:              ${Tools.toDisplayString(state.currentText)}")

            state.log("handleSemicolonHasBlock exit")
            return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(SingleBlock(state.header, state.footer, state.blockParts))))
        }

        fun handleSemicolonHasNoBlock(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val tempRemainingText = DotlinTools.substring(oldState.remainingText, 1) // removing the ";"
            if (DotlinTools.startsWith(tempRemainingText, "}"))
                return handleSemicolonHasNoBlockWithOpeningBraceNext(oldState)

            return handleSemicolonHasNoBlockWithoutOpeningBraceNext(oldState)
        }

        fun handleSemicolonHasNoBlockWithOpeningBraceNext(oldState: TextSplitterState): TextSplitterHandleResult
        {
            TODO("handleSemicolonHasNoBlockWithOpeningBraceNext")

            val state = oldState.clone()
            state.log("handleSemicolonHasNoBlockWithOpeningBrace")

            state.currentText += ";"
            DotlinLogger.log("currentText:              ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = DotlinTools.substring(state.remainingText, 1) // removing the ";"
            DotlinLogger.log("remainingText:            ${Tools.toDisplayString(state.remainingText)}")

            //state.footer = ""
            if (DotlinTools.startsWith(state.currentText, "}"))
            {
                /*if (!state.isSecondBlockWithBrackets)
                    TODO()*/

                state.footer = "}"
                state.currentText = DotlinTools.substring(state.currentText, 1) // removing the "}"
            }
            else
            {
                /*if (state.isSecondBlockWithBrackets)
                    TODO()*/
            }

            val elseEndPos = Tools.getElseEndPos(state.currentText)
            DotlinLogger.log("elseEndPos:                $elseEndPos")

            if (elseEndPos == -1)
                TODO("elseEndPos == -1")

            state.middle += DotlinTools.substring(state.currentText, 0, elseEndPos)
            DotlinLogger.log("middle:                    ${Tools.toDisplayString(state.middle)}")

            val statement = DotlinTools.substring(state.currentText, elseEndPos)
            DotlinLogger.log("statement:                 ${Tools.toDisplayString(statement)}")

            val parts2 = listOf(Statement(statement))

            return TextSplitterHandleResult(state, SplitResult("", listOf(DoubleBlock(state.header, state.middle, "", state.blockParts, parts2))))
        }

        fun handleSemicolonHasNoBlockWithoutOpeningBraceNext(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasNoBlockWithoutOpeningBrace")

            state.currentText += ";"
            DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = DotlinTools.substring(state.remainingText, 1) // removing the ";"
            DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            val elseEndPos = Tools.getElseEndPos(state.remainingText)
            DotlinLogger.log("elseEndPos:                $elseEndPos")

            if (elseEndPos == -1)
            {
                if (DotlinTools.startsWith(DotlinTools.trim(state.remainingText), "//"))
                {
                    val nextLinePos = Tools.getNextLinePos(state.remainingText)
                    DotlinLogger.log("nextLinePos:               $nextLinePos")
                    if (nextLinePos == -1)
                    {
                        state.currentText += state.remainingText
                        state.remainingText = ""
                    }
                    else
                    {
                        state.currentText += DotlinTools.substring(state.remainingText, 0, nextLinePos)
                        state.remainingText = DotlinTools.substring(state.remainingText, nextLinePos)
                    }

                    state.log("handleSemicolonHasNoBlockWithoutOpeningBrace exit-1-Statement")
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(Statement(state.currentText))))
                }

                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(Statement(state.currentText))))
            }

            state.currentText += DotlinTools.substring(state.remainingText, 0, elseEndPos)
            DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = DotlinTools.substring(state.remainingText, elseEndPos)
            DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            //state.isSecondBlockWithBrackets = DotlinTools.startsWith(state.remainingText, "{")

            state.log("handleSemicolonHasNoBlockWithoutOpeningBrace exit-2")
            return TextSplitterHandleResult(state, null)
        }
    }
}
