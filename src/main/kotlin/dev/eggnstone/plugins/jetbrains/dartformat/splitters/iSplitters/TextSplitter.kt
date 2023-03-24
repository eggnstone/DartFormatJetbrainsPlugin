package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement

class TextSplitter : ISplitter
{
    override val name = "Text"

    override fun split(inputText: String, params: SplitParams): SplitResult
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("TextSplitter.split: isEnum=${params.isEnum} ${Tools.toDisplayString(Tools.shorten(inputText, 100, true))}")

        if (StringWrapper.isEmpty(inputText))
            throw DartFormatException("Unexpected empty text.")

        var state = TextSplitterState(inputText)

        while (StringWrapper.isNotEmpty(state.remainingText))
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

            if (StringWrapper.startsWith(state.remainingText, "//") || StringWrapper.startsWith(state.remainingText, "/*"))
            {
                val handleResult = handleComment(state)
                if (handleResult.splitResult != null)
                    return handleResult.splitResult

                state = handleResult.state
                continue
            }

            if (currentChar == "=" && DotlinTools.isEmpty(state.currentBrackets))
            {
                state = handleEqualSign(state)
                continue
            }

            if (currentChar == ":")
            {
                state = handleColon(state)
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
            state.remainingText = StringWrapper.substring(state.remainingText, 1)
        }

        // Not returned yet and remaining text is empty => Empty block or only comments.
        if (state.currentText.isEmpty())
            TODO("untested: state.currentText.isEmpty()")

        if (state.commentOnlyHashCode != null && state.currentText.hashCode() == state.commentOnlyHashCode)
        {
            val statement = Statement(state.currentText)
            return SplitResult("", listOf(statement))
        }

        throw DartFormatException("Unexpected end of block or statement.")
    }

    companion object
    {
        private fun handleIsInApostrophes(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            if (StringWrapper.startsWith(state.remainingText, "\\'"))
            {
                state.currentText += "\\'"
                state.remainingText = StringWrapper.substring(state.remainingText, 2)
                return state
            }

            if (StringWrapper.startsWith(state.remainingText, "'"))
                state.isInApostrophes = false

            state.currentText += currentChar
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

            return state
        }

        private fun handleIsInNormalQuotes(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            if (StringWrapper.startsWith(state.remainingText, "\\\""))
            {
                state.currentText += "\\\""
                state.remainingText = StringWrapper.substring(state.remainingText, 2)
                return state
            }

            if (StringWrapper.startsWith(state.remainingText, "\""))
                state.isInNormalQuotes = false

            state.currentText += currentChar
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

            return state
        }

        private fun handleApostrophe(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.isInApostrophes = true
            state.currentText += "'"
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

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

                if (StringWrapper.isEmpty(state.currentText))
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf()))

                val statement = Statement(state.currentText)
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(statement)))
            }

            val lastOpeningBracket = state.currentBrackets.removeLast()
            val expectedClosingBracket = Tools.getClosingBracket(lastOpeningBracket)
            if (currentChar != expectedClosingBracket)
                TODO("handleClosingBracket: ${Tools.toDisplayString(state.remainingText)}")

            state.currentText += currentChar
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

            return TextSplitterHandleResult(state, null)
        }

        private fun handleComment(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleComment")

            val indentOfLastLine = Tools.getIndentOfLastLine(state.currentText)
            DotlinLogger.log("indentOfLastLine: $indentOfLastLine")

            if (DotlinLogger.isEnabled) DotlinLogger.log("Calling CommentExtractor ..")
            val extractionResult = CommentExtractor.extract(state.remainingText, indentOfLastLine)

            if (DotlinLogger.isEnabled)
            {
                DotlinLogger.log("Result from CommentExtractor:")
                DotlinLogger.log("  comment        ${Tools.toDisplayString(extractionResult.comment)}")
                DotlinLogger.log("  remainingText: ${Tools.toDisplayString(extractionResult.remainingText)}")
            }

            if (DotlinLogger.isEnabled) DotlinLogger.log("commentOnlyHashCode:       ${state.commentOnlyHashCode}")
            if (state.commentOnlyHashCode == null)
            {
                if (state.currentText.isEmpty())
                {
                    state.remainingText = extractionResult.remainingText
                    if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")
                    // remove? state.commentOnlyHashCode = extractionResult.comment.hashCode()
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(Comment(extractionResult.comment, 0))))
                }
            }
            else
            {
                if (state.currentText.hashCode() == state.commentOnlyHashCode)
                    state.commentOnlyHashCode = (state.currentText + extractionResult.comment).hashCode()
                else
                    state.commentOnlyHashCode = null
            }
            if (DotlinLogger.isEnabled) DotlinLogger.log("commentOnlyHashCode:       ${state.commentOnlyHashCode}")

            state.currentText += extractionResult.comment
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")

            state.remainingText = extractionResult.remainingText
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            return TextSplitterHandleResult(state, null)
        }

        private fun handleEqualSign(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()
            state.log("handleEqualSign")

            if (state.hasColon)
            {
                if (DotlinLogger.isEnabled) DotlinLogger.log("Ignoring '='")
            }
            else
                state.isInAssignment = true

            state.currentText += "="
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

            return state
        }

        private fun handleNormalQuotes(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.isInNormalQuotes = true
            state.currentText += "\""
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

            return state
        }

        private fun handleOpeningBracket(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.currentBrackets.add(currentChar)
            state.currentText += currentChar
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

            return state
        }

        fun handleOpeningBrace(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleOpeningBrace")

            state.currentText += "{"
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1)
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            val params = SplitParams(isEnum = StringWrapper.startsWith(state.currentText, "enum "), expectClosingBrace = true)
            if (DotlinLogger.isEnabled) DotlinLogger.log("params.isEnum:             ${params.isEnum}")

            if (DotlinLogger.isEnabled) DotlinLogger.log("-> MasterSplitter.split(   ${Tools.toDisplayString(state.remainingText)})")
            val result = MasterSplitter().split(state.remainingText, params)
            if (DotlinLogger.isEnabled)
            {
                DotlinLogger.log("<- MasterSplitter.split(   ${Tools.toDisplayString(state.remainingText)})")
                DotlinLogger.log("  remainingText:           ${Tools.toDisplayString(result.remainingText)}")
                DotlinLogger.log("  parts:                   ${Tools.toDisplayStringForParts(result.parts)}")
            }

            state.remainingText = result.remainingText

            if (!StringWrapper.startsWith(state.remainingText, "}"))
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

                state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the "}"
                state.middle += "{"
                state.footer = "}"

                state.log("handleOpeningBrace exit-2-DoubleBlock")
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(DoubleBlock(state.header, state.middle, state.footer, state.blockParts, result.parts))))
            }

            if (state.remainingText == "}")
            {
                //if (DotlinLogger.isEnabled) DotlinLogger.log("- Returning SingleBlock (remainingText == \"}\")")
                state.log("handleOpeningBrace exit-2-SingleBlock")
                return TextSplitterHandleResult(state, SplitResult("", listOf(SingleBlock(state.currentText, "}", result.parts))))
            }

            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the "}"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            val elseEndPos = Tools.getElseEndPos(state.remainingText)
            if (DotlinLogger.isEnabled) DotlinLogger.log("elseEndPos:                $elseEndPos")

            if (elseEndPos == -1)
            {
                state.footer = "}"
                state.log("handleOpeningBrace exit-3-SingleBlock")
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(SingleBlock(state.currentText, state.footer, result.parts))))
            }

            state.middle = "}" + StringWrapper.substring(state.remainingText, 0, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("middle:                    ${Tools.toDisplayString(state.middle)}")

            state.remainingText = StringWrapper.substring(state.remainingText, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            if (StringWrapper.isEmpty(state.remainingText))
                throw DartFormatException("TODO")

            state.hasBlock = true
            state.header = state.currentText
            state.blockParts = result.parts
            state.currentText = ""

            state.log("handleOpeningBrace exit-4")
            return TextSplitterHandleResult(state, null)
        }

        fun handleColon(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()
            state.log("handleColon")

            /*if (state.hasColon)
                TODO()*/

            state.hasColon = true

            state.currentText += ":"
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            return state
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
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            state.footer = state.middle + state.currentText
            if (DotlinLogger.isEnabled) DotlinLogger.log("footer:                   ${Tools.toDisplayString(state.footer)}")
            state.middle = ""
            if (DotlinLogger.isEnabled) DotlinLogger.log("middle:                   ${Tools.toDisplayString(state.middle)}")
            state.currentText = ""
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:              ${Tools.toDisplayString(state.currentText)}")

            state.log("handleSemicolonHasBlock exit")
            return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(SingleBlock(state.header, state.footer, state.blockParts))))
        }

        private fun handleSemicolonHasNoBlock(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val tempRemainingText = StringWrapper.substring(oldState.remainingText, 1) // removing the ";"
            if (StringWrapper.startsWith(tempRemainingText, "}"))
                TODO("TextSplitter.handleSemicolonHasNoBlock") // return handleSemicolonHasNoBlockWithOpeningBraceNext(oldState)

            return handleSemicolonHasNoBlockWithoutOpeningBraceNext(oldState)
        }

        private fun handleSemicolonHasNoBlockWithoutOpeningBraceNext(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasNoBlockWithoutOpeningBrace")

            state.currentText += ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            val elseEndPos = Tools.getElseEndPos(state.remainingText)
            if (DotlinLogger.isEnabled) DotlinLogger.log("elseEndPos:                $elseEndPos")

            if (elseEndPos == -1)
            {
                if (StringWrapper.startsWith(StringWrapper.trim(state.remainingText), "//"))
                {
                    val nextLinePos = Tools.getNextLinePos(state.remainingText)
                    if (DotlinLogger.isEnabled) DotlinLogger.log("nextLinePos:               $nextLinePos")
                    if (nextLinePos == -1)
                    {
                        state.currentText += state.remainingText
                        state.remainingText = ""
                    }
                    else
                    {
                        state.currentText += StringWrapper.substring(state.remainingText, 0, nextLinePos)
                        state.remainingText = StringWrapper.substring(state.remainingText, nextLinePos)
                    }

                    state.log("handleSemicolonHasNoBlockWithoutOpeningBrace exit-1-Statement")
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(Statement(state.currentText))))
                }

                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(Statement(state.currentText))))
            }

            state.currentText += StringWrapper.substring(state.remainingText, 0, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayString(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayString(state.remainingText)}")

            //state.isSecondBlockWithBrackets = StringWrapper.startsWith(state.remainingText, "{")

            state.log("handleSemicolonHasNoBlockWithoutOpeningBrace exit-2")
            return TextSplitterHandleResult(state, null)
        }
    }
}
