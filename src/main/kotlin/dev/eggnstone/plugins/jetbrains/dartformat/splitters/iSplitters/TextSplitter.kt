package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

class TextSplitter : ISplitter
{
    override val name = "Text"

    companion object
    {
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
                {
                    val statement = Statement(state.currentText)
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(statement)))
                }

                if (!params.expectClosingBrace)
                    throw DartFormatException("TextSplitter.handleClosingBracket: Unexpected closing bracket: currentChar=${Tools.toDisplayStringShort(currentChar)} remainingText=${Tools.toDisplayStringShort(state.remainingText)}")

                if (StringWrapper.isEmpty(state.currentText))
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf()))

                val statement = Statement(state.currentText)
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(statement)))
            }

            val lastOpeningBracket = state.currentBrackets.removeLast()
            val expectedClosingBracket = Tools.getClosingBracket(lastOpeningBracket)
            if (currentChar != expectedClosingBracket)
                TODO("handleClosingBracket: ${Tools.toDisplayStringShort(state.remainingText)}")

            state.currentText += currentChar
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

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
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            return state
        }

        private fun handleComment(oldState: TextSplitterState, currentIndent: Int): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleComment")

            /*val indentOfLastLine = Tools.getIndentOfLastLine(state.currentText)
            DotlinLogger.log("indentOfLastLine: $indentOfLastLine")*/

            if (DotlinLogger.isEnabled) DotlinLogger.log("Calling CommentExtractor ...")
            val extractionResult = CommentExtractor.extract(state.remainingText, currentIndent)

            if (DotlinLogger.isEnabled)
            {
                DotlinLogger.log("Result from CommentExtractor:")
                DotlinLogger.log("  comment        ${Tools.toDisplayStringShort(extractionResult.comment)}")
                DotlinLogger.log("  remainingText: ${Tools.toDisplayStringShort(extractionResult.remainingText)}")
                DotlinLogger.log("  startPos:      ${extractionResult.startPos}")
            }

            if (DotlinLogger.isEnabled) DotlinLogger.log("commentOnlyHashCode:       ${state.commentOnlyHashCode}")
            if (state.commentOnlyHashCode == null)
            {
                if (state.currentText.isEmpty())
                {
                    state.remainingText = extractionResult.remainingText
                    if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")
                    // remove? state.commentOnlyHashCode = extractionResult.comment.hashCode()
                    val comment = Comment(extractionResult.comment, extractionResult.startPos)
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(comment)))
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
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(state.currentText)}")

            state.remainingText = extractionResult.remainingText
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

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

        private fun handleNormalQuotes(oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.isInNormalQuotes = true
            state.currentText += "\""
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

            return state
        }

        fun handleOpeningBrace(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleOpeningBrace")

            state.currentText += "{"
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1)
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            val params = SplitParams(isEnum = StringWrapper.startsWith(state.currentText, "enum "), expectClosingBrace = true)
            if (DotlinLogger.isEnabled) DotlinLogger.log("params.isEnum:             ${params.isEnum}")

            if (DotlinLogger.isEnabled) DotlinLogger.log("-> MasterSplitter.split(   ${Tools.toDisplayStringShort(state.remainingText)})")
            val result = MasterSplitter().split(state.remainingText, params)
            if (DotlinLogger.isEnabled)
            {
                DotlinLogger.log("<- MasterSplitter.split(   ${Tools.toDisplayStringShort(state.remainingText)})")
                DotlinLogger.log("  remainingText:           ${Tools.toDisplayStringShort(result.remainingText)}")
                DotlinLogger.log("  parts:                   ${Tools.toDisplayStringForParts(result.parts)}")
            }

            state.remainingText = result.remainingText

            if (!StringWrapper.startsWith(state.remainingText, "}"))
            {
                oldState.log("handleOpeningBrace - Missing closing brace (old)")
                state.log("handleOpeningBrace - Missing closing brace (new)")
                DotlinLogger.log("result.remainingText: ${Tools.toDisplayStringShort(result.remainingText)}")
                DotlinLogger.log("result.parts: ${Tools.toDisplayStringForParts(result.parts)}")
                throw DartFormatException("Missing closing brace: ${Tools.toDisplayStringShort(state.remainingText)}")
            }

            if (state.hasBlockOLD)
            {
                if (state.remainingText == "}")
                {
                    state.remainingText = ""
                    state.middleOLD += "{"
                    state.footer = "}"

                    state.log("handleOpeningBrace exit-1-DoubleBlock")
                    val doubleBlock = MultiBlock.double(state.headerOLD, state.middleOLD, state.footer, state.blockPartsOLD, result.parts)
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(doubleBlock)))
                }

                // starts with "}" but has more

                state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the "}"
                state.middleOLD += "{"
                state.footer = "}"

                state.log("handleOpeningBrace exit-2-DoubleBlock")
                val doubleBlock = MultiBlock.double(state.headerOLD, state.middleOLD, state.footer, state.blockPartsOLD, result.parts)
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(doubleBlock)))
            }

            if (state.remainingText == "}")
            {
                //if (DotlinLogger.isEnabled) DotlinLogger.log("- Returning SingleBlock (remainingText == \"}\")")
                state.log("handleOpeningBrace exit-2-SingleBlock")
                val singleBlock = MultiBlock.single(state.currentText, "}", result.parts)
                return TextSplitterHandleResult(state, SplitResult("", listOf(singleBlock)))
            }

            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the "}"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            val elseEndPos = Tools.getElseEndPos(state.remainingText)
            if (DotlinLogger.isEnabled) DotlinLogger.log("elseEndPos:                $elseEndPos")

            if (elseEndPos == -1)
            {
                state.footer = "}"
                state.log("handleOpeningBrace exit-3-SingleBlock")
                val singleBlock = MultiBlock.single(state.currentText, state.footer, result.parts)
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(singleBlock)))
            }

            state.middleOLD = "}" + StringWrapper.substring(state.remainingText, 0, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("middle:                    ${Tools.toDisplayStringShort(state.middleOLD)}")

            state.remainingText = StringWrapper.substring(state.remainingText, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            if (StringWrapper.isEmpty(state.remainingText))
                throw DartFormatException("StringWrapper.isEmpty(state.remainingText)")

            state.hasBlockOLD = true
            state.headerOLD = state.currentText
            state.blockPartsOLD = result.parts

            state.headers.add(state.currentText)
            state.parts.add(result.parts)

            state.currentText = ""

            state.log("handleOpeningBrace exit-4")
            return TextSplitterHandleResult(state, null)
        }

        private fun handleOpeningBracket(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.currentBrackets.add(currentChar)
            state.currentText += currentChar
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

            return state
        }

        fun handleSemicolon(oldState: TextSplitterState): TextSplitterHandleResult
        {
            if (oldState.hasBlockOLD)
                return handleSemicolonHasBlock(oldState)

            return handleSemicolonHasNoBlock(oldState)
        }

        fun handleSemicolonHasBlock(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasBlock")

            state.currentText += ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            state.footer = state.middleOLD + state.currentText
            if (DotlinLogger.isEnabled) DotlinLogger.log("footer:                   ${Tools.toDisplayStringShort(state.footer)}")
            state.middleOLD = ""
            if (DotlinLogger.isEnabled) DotlinLogger.log("middle:                   ${Tools.toDisplayStringShort(state.middleOLD)}")
            state.currentText = ""
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:              ${Tools.toDisplayStringShort(state.currentText)}")

            state.log("handleSemicolonHasBlock exit")
            val singleBlock = MultiBlock.single(state.headerOLD, state.footer, state.blockPartsOLD)
            return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(singleBlock)))
        }

        private fun handleSemicolonHasNoBlock(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasNoBlock")

            val tempRemainingText = StringWrapper.substring(oldState.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("tempRemainingText: ${Tools.toDisplayStringShort(tempRemainingText)}")

            if (StringWrapper.startsWith(tempRemainingText, "}"))
                return handleSemicolonHasNoBlockWithClosingBraceNext(oldState)

            return handleSemicolonHasNoBlockWithoutClosingBraceNext(oldState)
        }

        private fun handleSemicolonHasNoBlockWithClosingBraceNext(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasNoBlockWithClosingBraceNext")

            state.currentText += ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            val statement = Statement(state.currentText)
            return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(statement)))
        }

        private fun handleSemicolonHasNoBlockWithoutClosingBraceNext(oldState: TextSplitterState): TextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasNoBlockWithoutClosingBraceNext")

            state.currentText += ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

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
                    val statement = Statement(state.currentText)
                    return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(statement)))
                }

                val statement = Statement(state.currentText)
                return TextSplitterHandleResult(state, SplitResult(state.remainingText, listOf(statement)))
            }

            state.currentText += StringWrapper.substring(state.remainingText, 0, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            state.log("handleSemicolonHasNoBlockWithoutOpeningBrace exit-2")
            return TextSplitterHandleResult(state, null)
        }
    }

    override fun split(inputText: String, params: SplitParams, inputCurrentIndent: Int): SplitResult
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("TextSplitter.split: inputCurrentIndent=$inputCurrentIndent isEnum=${params.isEnum} ${Tools.toDisplayStringShort(inputText)}")

        if (StringWrapper.isEmpty(inputText))
            throw DartFormatException("Unexpected empty text.")

        val currentIndent = inputCurrentIndent
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
                val handleResult = handleComment(state, currentIndent)
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
            TODO("TextSplitter.split: state.currentText.isEmpty()")

        if (state.commentOnlyHashCode != null && state.currentText.hashCode() == state.commentOnlyHashCode)
        {
            TODO("TextSplitter.split: state.commentOnlyHashCode != null && state.currentText.hashCode() == state.commentOnlyHashCode")
            val statement = Statement("COMMENT?" + state.currentText)
            return SplitResult("", listOf(statement))
        }

        val pos = inputText.length - state.remainingText.length
        throw DartFormatException("At position $pos: unexpected end of text: " + Tools.toDisplayStringShort(state.remainingText))
    }
}
