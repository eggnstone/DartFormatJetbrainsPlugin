package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
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

        private fun handleClosingBracket(currentChar: String, oldState: TextSplitterState, params: SplitParams): ITextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleClosingBracket", params)

            if (DotlinTools.isEmpty(state.currentBrackets))
            {
                if (params.isEnum)
                {
                    val statement = Statement(state.currentText)
                    return TextSplitterHandleSplitResult(SplitResult(state.remainingText, listOf(statement)))
                }

                if (!params.expectClosingBrace)
                    throw DartFormatException("TextSplitter.handleClosingBracket: Unexpected closing bracket: currentChar=${Tools.toDisplayStringShort(currentChar)} remainingText=${Tools.toDisplayStringShort(state.remainingText)}")

                if (StringWrapper.isEmpty(state.currentText))
                    return TextSplitterHandleSplitResult(SplitResult(state.remainingText, listOf()))

                val statement = Statement(state.currentText)
                return TextSplitterHandleSplitResult(SplitResult(state.remainingText, listOf(statement)))
            }

            val lastOpeningBracket = state.currentBrackets.removeLast()
            val expectedClosingBracket = Tools.getClosingBracket(lastOpeningBracket)
            if (currentChar != expectedClosingBracket)
                TODO("handleClosingBracket: ${Tools.toDisplayStringShort(state.remainingText)}")

            state.currentText += currentChar
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

            return TextSplitterHandleStateResult(state)
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

        private fun handleComment(oldState: TextSplitterState, currentIndent: Int): ITextSplitterHandleResult
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
                    if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(extractionResult.remainingText)}")
                    val comment = Comment(extractionResult.comment, extractionResult.startPos)
                    return TextSplitterHandleSplitResult(SplitResult(extractionResult.remainingText, listOf(comment)))
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

            return TextSplitterHandleStateResult(state)
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

        fun handleOpeningBrace(oldState: TextSplitterState): ITextSplitterHandleResult
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

            return handleClosingBrace(state, result.parts)
        }

        private fun handleClosingBrace(oldState: TextSplitterState, parts: List<IPart>): ITextSplitterHandleResult
        {
            if (DotlinLogger.isEnabled) DotlinLogger.log("TextSplitter.handleClosingBrace: parts: ${Tools.toDisplayStringForParts(parts)}")

            val state = oldState.clone()
            state.log("handleClosingBrace")

            if (state.remainingText == "}")
            {
                state.remainingText = ""
                state.headers.add(state.currentText)
                state.partLists.add(parts)
                state.footer = "}"
                state.log("handleClosingBrace exit-1")

                val multiBlock = MultiBlock(state.headers, state.partLists, state.footer)
                return TextSplitterHandleSplitResult(SplitResult(state.remainingText, listOf(multiBlock)))
            }

            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the "}"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            val elseEndPos = Tools.getElseEndPos(state.remainingText)
            if (DotlinLogger.isEnabled) DotlinLogger.log("elseEndPos:                $elseEndPos")

            if (elseEndPos == -1)
            {
                state.log("handleClosingBrace exit-2-pre")

                //state.remainingText = ""
                state.headers.add(state.currentText)
                state.partLists.add(parts)
                state.footer = "}"
                state.log("handleClosingBrace exit-2")

                val multiBlock = MultiBlock(state.headers, state.partLists, state.footer)
                return TextSplitterHandleSplitResult(SplitResult(state.remainingText, listOf(multiBlock)))
            }

            val tempLeadingText = StringWrapper.substring(state.remainingText, 0, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("tempLeadingText:           ${Tools.toDisplayStringShort(tempLeadingText)}")
            val tempRemainingText = StringWrapper.substring(state.remainingText, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("tempRemainingText:         ${Tools.toDisplayStringShort(tempRemainingText)}")

            val ifEndPos = Tools.getTextEndPos(tempRemainingText, "if")
            if (DotlinLogger.isEnabled) DotlinLogger.log("ifEndPos:                  $ifEndPos")

            if (ifEndPos == -1)
            {
                state.log("elseEndPos >= 0 && ifEndPos == -1")

                val header = state.currentText
                if (DotlinLogger.isEnabled) DotlinLogger.log("header:                    ${Tools.toDisplayStringShort(header)}")

                if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

                state.headers.add(header)
                state.partLists.add(parts)

                state.currentText = "}"

                state.log("handleClosingBrace exit-3")
                return TextSplitterHandleStateResult(state)
            }

            state.log("elseEndPos >= 0 && ifEndPos >= 0")

            val elseIfResult = MasterSplitter().split(tempRemainingText, params = SplitParams(), inputCurrentIndent = 0, stopAfter = 1)
            if (DotlinLogger.isEnabled) DotlinLogger.log("tempRemainingText:         ${Tools.toDisplayStringShort(tempRemainingText)}")
            if (DotlinLogger.isEnabled) DotlinLogger.log("elseIfResult:              $elseIfResult")

            val header = state.currentText
            if (DotlinLogger.isEnabled) DotlinLogger.log("header:                    ${Tools.toDisplayStringShort(header)}")

            state.remainingText = elseIfResult.remainingText
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            state.headers.add(header)
            state.partLists.add(parts)

            state.currentText = ""

            state.log("handleClosingBrace exit-4")

            if (elseIfResult.parts.size != 1)
                throw DartFormatException("elseIfResult.parts.size (${elseIfResult.parts.size}) != 1")

            val elseIfPart = elseIfResult.parts[0]
            if (elseIfPart is MultiBlock)
            {
                val elseIfMultiBlock: MultiBlock = elseIfPart

                val headers = mutableListOf(header)
                for (i in 0 until elseIfMultiBlock.headers.size)
                {
                    val headerI = elseIfMultiBlock.headers[i]
                    if (i == 0)
                        headers.add("}$tempLeadingText$headerI")
                    else
                        headers.add(headerI)
                }

                val partLists = mutableListOf(parts)
                partLists.addAll(elseIfMultiBlock.partLists)
                val footer = elseIfMultiBlock.footer
                //val footer = "}TODO"+elseIfMultiBlock.footer+"END"

                val multiBlock = MultiBlock(headers, partLists, footer)
                return TextSplitterHandleSplitResult(SplitResult(elseIfResult.remainingText, listOf(multiBlock)))
            }

            if (elseIfPart is Statement)
            {
                val elseIfStatement: Statement = elseIfPart

                val headers = mutableListOf(header)
                val partLists = mutableListOf(parts)
                val footer = "}$tempLeadingText${elseIfStatement.text}"

                val multiBlock = MultiBlock(headers, partLists, footer)
                return TextSplitterHandleSplitResult(SplitResult(elseIfResult.remainingText, listOf(multiBlock)))
            }

            TODO("elseIfPart is ?: ${elseIfPart::class.simpleName}")
        }

        private fun handleOpeningBracket(currentChar: String, oldState: TextSplitterState): TextSplitterState
        {
            val state = oldState.clone()

            state.currentBrackets.add(currentChar)
            state.currentText += currentChar
            state.remainingText = StringWrapper.substring(state.remainingText, 1)

            return state
        }

        fun handleSemicolon(oldState: TextSplitterState): ITextSplitterHandleResult
        {
            if (oldState.partLists.size > 0)
                return handleSemicolonHasBlocks(oldState)

            return handleSemicolonHasNoBlock(oldState)
        }

        fun handleSemicolonHasBlocks(oldState: TextSplitterState): ITextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasBlocks")

            state.currentText += ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            state.footer = /*"state.middleOLD" + */state.currentText
            if (DotlinLogger.isEnabled) DotlinLogger.log("footer:                   ${Tools.toDisplayStringShort(state.footer)}")
            //state.middleOLD = ""
            //if (DotlinLogger.isEnabled) DotlinLogger.log("middle:                   ${Tools.toDisplayStringShort(state.middleOLD)}")
            state.currentText = ""
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:              ${Tools.toDisplayStringShort(state.currentText)}")

            if (state.headers.size == 0)
                throw DartFormatException("state.headers.size == 0")

            if (state.headers.size != state.partLists.size)
                throw DartFormatException("state.headers.size (${state.headers.size}) != state.partLists.size (${state.partLists.size})")

            state.log("handleSemicolonHasBlocks exit")
            val multiBlock = MultiBlock(state.headers, state.partLists, state.footer)
            return TextSplitterHandleSplitResult(SplitResult(state.remainingText, listOf(multiBlock)))
        }

        private fun handleSemicolonHasNoBlock(oldState: TextSplitterState): ITextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasNoBlock")

            val tempRemainingText = StringWrapper.substring(oldState.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("tempRemainingText: ${Tools.toDisplayStringShort(tempRemainingText)}")

            if (StringWrapper.startsWith(tempRemainingText, "}"))
                return handleSemicolonHasNoBlockWithClosingBraceNext(oldState)

            return handleSemicolonHasNoBlockWithoutClosingBraceNext(oldState)
        }

        private fun handleSemicolonHasNoBlockWithClosingBraceNext(oldState: TextSplitterState): ITextSplitterHandleResult
        {
            val state = oldState.clone()
            state.log("handleSemicolonHasNoBlockWithClosingBraceNext")

            state.currentText += ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, 1) // removing the ";"
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            val statement = Statement(state.currentText)
            return TextSplitterHandleSplitResult(SplitResult(state.remainingText, listOf(statement)))
        }

        private fun handleSemicolonHasNoBlockWithoutClosingBraceNext(oldState: TextSplitterState): ITextSplitterHandleResult
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
                    return TextSplitterHandleSplitResult(SplitResult(state.remainingText, listOf(statement)))
                }

                val statement = Statement(state.currentText)
                return TextSplitterHandleSplitResult(SplitResult(state.remainingText, listOf(statement)))
            }

            state.currentText += StringWrapper.substring(state.remainingText, 0, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentText:               ${Tools.toDisplayStringShort(state.currentText)}")
            state.remainingText = StringWrapper.substring(state.remainingText, elseEndPos)
            if (DotlinLogger.isEnabled) DotlinLogger.log("remainingText:             ${Tools.toDisplayStringShort(state.remainingText)}")

            state.log("handleSemicolonHasNoBlockWithoutOpeningBrace exit-2")
            return TextSplitterHandleStateResult(state)
        }
    }

    override fun split(inputText: String, params: SplitParams, inputCurrentIndent: Int, stopAfter: Int): SplitResult
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
                when (val handleResult = handleComment(state, currentIndent))
                {
                    is TextSplitterHandleSplitResult -> return handleResult.splitResult
                    is TextSplitterHandleStateResult ->
                    {
                        state = handleResult.state
                        continue
                    }
                }
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
                when (val handleResult = handleSemicolon(state))
                {
                    is TextSplitterHandleSplitResult -> return handleResult.splitResult
                    is TextSplitterHandleStateResult ->
                    {
                        state = handleResult.state
                        continue
                    }
                }
            }

            if (currentChar == "{" && !state.isInAssignment && DotlinTools.isEmpty(state.currentBrackets))
            {
                when (val handleResult = handleOpeningBrace(state))
                {
                    is TextSplitterHandleSplitResult -> return handleResult.splitResult
                    is TextSplitterHandleStateResult ->
                    {
                        state = handleResult.state
                        continue
                    }
                }
            }

            if (Tools.isOpeningBracket(currentChar))
            {
                state = handleOpeningBracket(currentChar, state)
                continue
            }

            if (Tools.isClosingBracket(currentChar))
            {
                when (val handleResult = handleClosingBracket(currentChar, state, params))
                {
                    is TextSplitterHandleSplitResult -> return handleResult.splitResult
                    is TextSplitterHandleStateResult ->
                    {
                        state = handleResult.state
                        continue
                    }
                }
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
