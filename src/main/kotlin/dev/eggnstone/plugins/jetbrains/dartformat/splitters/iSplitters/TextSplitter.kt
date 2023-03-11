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
    override fun split(inputText: String): SplitResult
    {
        DotlinLogger.log("TextSplitter.split: ${Tools.toDisplayString(Tools.shorten(inputText, 100, true))}")

        if (DotlinTools.isEmpty(inputText))
            throw DartFormatException("Unexpected empty text.")

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

            if (DotlinTools.startsWith(state.remainingText, "//") || DotlinTools.startsWith(state.remainingText, "/*"))
            {
                state = handleComment(state)
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
                val handleResult = handleOpeningCurlyBracket(state)
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

        private fun handleApostrophe(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.isInApostrophes = true
            state.currentText += "'"
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

        private fun handleOpeningCurlyBracket(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleOpeningCurlyBracket")

            state.currentText += "{"
            state.remainingText = DotlinTools.substring(state.remainingText, 1)

            val result = MasterSplitter().split(state.remainingText)
            state.remainingText = result.remainingText

            if (!DotlinTools.startsWith(state.remainingText, "}"))
            {
                state.log("4")
                TODO("error 1") // ${Tools.toDisplayString(currentText)} ${Tools.toDisplayString(header)} ${Tools.toDisplayString(remainingText)}")
            }

            if (state.hasBlock)
            {
                if (state.remainingText == "}")
                {
                    state.log("handleOpeningCurlyBracket exit-1-DoubleBlock")

                    state.middle += "{"
                    state.footer = "}"
                    //state.footer = state.middle + state.currentText
                    //state.middle = ""

                    /*DotlinLogger.log("footer:                   ${Tools.toDisplayString(state.footer)}")
                    state.middle = ""
                    DotlinLogger.log("middle:                   ${Tools.toDisplayString(state.middle)}")
                    state.currentText = ""
                    DotlinLogger.log("currentText:              ${Tools.toDisplayString(state.currentText)}")*/

                    return TextSplitterHandleResult(state, SplitResult("", listOf(DoubleBlock(state.header, state.middle, state.footer, state.blockParts, result.parts))))
                }

                state.log("5")
                TODO("is double block")
            }

            if (state.remainingText == "}")
            {
                //DotlinLogger.log("- Returning SingleBlock (remainingText == \"}\")")
                state.log("handleOpeningCurlyBracket exit-2-SingleBlock")
                return TextSplitterHandleResult(state, SplitResult("", listOf(SingleBlock(state.currentText, "}", result.parts))))
            }

            state.remainingText = DotlinTools.substring(state.remainingText, 1) // removing the "}"
            DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            val elseEndPos = Tools.getElseEndPos(state.remainingText)
            DotlinLogger.log("elseEndPos:                $elseEndPos")

            if (elseEndPos == -1)
            {
                state.footer = "}"
                state.log("handleOpeningCurlyBracket exit-3-SingleBlock")
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

            state.log("handleOpeningCurlyBracket exit-4")
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

            /*//state.footer = ""
            if (DotlinTools.startsWith(state.currentText, "}"))
            {
                *//*if (!state.isSecondBlockWithBrackets)
                    TODO()*//*

                state.footer = "}"
                state.currentText = DotlinTools.substring(state.currentText, 1) // removing the "}"
            }
            else
            {
                *//*if (state.isSecondBlockWithBrackets)
                    TODO()*//*
            }*/

            /*val elseEndPos = Tools.getElseEndPos(state.currentText)
            DotlinLogger.log("elseEndPos:                $elseEndPos")


            if (elseEndPos == -1)
                TODO("elseEndPos == -1")*/

            /*state.log("handleSemicolonHasBlock 1")

            state.middle += DotlinTools.substring(state.currentText, 0, elseEndPos)
            DotlinLogger.log("middle:                    ${Tools.toDisplayString(state.middle)}")*/

            //val statement = DotlinTools.substring(state.currentText, elseEndPos)
            //DotlinLogger.log("statement:                 ${Tools.toDisplayString(statement)}")

            //val parts2 = listOf(Statement(state.currentText))

            state.footer = state.middle + state.currentText
            DotlinLogger.log("footer:                   ${Tools.toDisplayString(state.footer)}")
            state.middle = ""
            DotlinLogger.log("middle:                   ${Tools.toDisplayString(state.middle)}")
            state.currentText = ""
            DotlinLogger.log("currentText:              ${Tools.toDisplayString(state.currentText)}")

            state.log("handleSemicolonHasBlock exit")
            return TextSplitterHandleResult(state, SplitResult("", listOf(SingleBlock(state.header, state.footer, state.blockParts))))
        }

        fun handleSemicolonHasNoBlock(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val tempRemainingText = DotlinTools.substring(oldState.remainingText, 1) // removing the ";"
            if (DotlinTools.startsWith(tempRemainingText, "}"))
                return handleSemicolonHasNoBlockWithOpeningCurlyBracketNext(oldState)

            return handleSemicolonHasNoBlockWithoutOpeningCurlyBracketNext(oldState)
        }

        fun handleSemicolonHasNoBlockWithOpeningCurlyBracketNext(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasNoBlockWithOpeningCurlyBracket")

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

        fun handleSemicolonHasNoBlockWithoutOpeningCurlyBracketNext(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasNoBlockWithoutOpeningCurlyBracket")

            state.currentText += ";"
            DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = DotlinTools.substring(state.remainingText, 1) // removing the ";"
            DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            val elseEndPos = Tools.getElseEndPos(state.remainingText)
            DotlinLogger.log("elseEndPos:                $elseEndPos")

            if (elseEndPos == -1)
            {
                state.log("handleSemicolonHasNoBlockWithoutOpeningCurlyBracket exit-1-Statement")
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(Statement(state.currentText))))
            }

            state.currentText += DotlinTools.substring(state.remainingText, 0, elseEndPos)
            DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = DotlinTools.substring(state.remainingText, elseEndPos)
            DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            //state.isSecondBlockWithBrackets = DotlinTools.startsWith(state.remainingText, "{")

            state.log("handleSemicolonHasNoBlockWithoutOpeningCurlyBracket exit-2")
            return TextSplitterHandleResult(state, null)
        }
    }
}
