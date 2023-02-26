package com.eggnstone.jetbrainsplugins.dartformat.blockifier

import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld
import com.eggnstone.jetbrainsplugins.dartformat.blocks.*
import com.eggnstone.jetbrainsplugins.dartformat.dotlin.DotlinLogger

class BlockifierOld
{
    companion object
    {
        const val debug = false
    }

    fun printBlocks(blocks: List<IBlock>)
    {
        for (block in blocks)
        {
            DotlinLogger.log("Block: ${block::class.simpleName}")
            DotlinLogger.log("  $block")
        }
    }

    fun blockify(text: String): List<IBlock>
    {
        var state = BlockifierStateOld()

        for (c in text)
        {
            if (debug)
                DotlinLogger.log("${ToolsOld.toDisplayString2(c)} ${state.currentType} ${ToolsOld.toDisplayString2(state.currentText)}")

            if (state.currentType != AreaType.Unknown)
            {
                state = when (state.currentType)
                {
                    AreaType.ClassBody -> handleInClassBody(c, state)
                    AreaType.ClassHeader -> handleInClassHeader(c, state)
                    AreaType.CurlyBracket -> handleInCurlyBrackets(c, state)
                    AreaType.Whitespace -> handleInWhitespace(c, state)
                    else -> throw DartFormatException("Unhandled BlockType: ${state.currentType}")
                }
                continue
            }

            if (ToolsOld.isWhitespace(c))
            {
                @Suppress("ReplaceSizeZeroCheckWithIsEmpty") // dotlin
                if (state.currentText.length == 0)
                {
                    state.currentType = AreaType.Whitespace
                    if (debug)
                        DotlinLogger.log("  -> ${state.currentType}")

                    state.currentText += c
                    continue
                }

                if (state.currentText == "class" || state.currentText == "abstract class")
                {
                    state.currentType = AreaType.ClassHeader
                    if (debug)
                        DotlinLogger.log("  -> ${state.currentType}")
                }

                state.currentText += c
                continue
            }

            if (c == '{')
            {
                @Suppress("ReplaceSizeZeroCheckWithIsEmpty") // dotlin
                if (state.currentText.length == 0)
                {
                    state.currentType = AreaType.CurlyBracket
                    if (debug)
                        DotlinLogger.log("  -> ${state.currentType}")

                    state.currentText += c
                    continue
                }
            }

            if (c == ';')
            {
                state.blocks.add(ExpressionBlock(state.currentText + c)) // dotlin
                state.currentText = ""
                continue
            }

            state.currentText += c
        }

        if (state.currentText.isNotEmpty())
        {
            if (state.currentType == AreaType.CurlyBracket)
                state.blocks.add(CurlyBracketBlock(mutableListOf(UnknownBlock(state.currentText)))) // dotlin
            else if (state.currentType == AreaType.Unknown)
                state.blocks.add(UnknownBlock(state.currentText)) // dotlin
            else if (state.currentType == AreaType.Whitespace)
                state.blocks.add(WhitespaceBlock(state.currentText)) // dotlin
            else
                throw DartFormatException("Unhandled BlockType at end of text: ${state.currentType}")
        }

        return state.blocks
    }

    private fun handleInClassBody(c: Char, state: BlockifierStateOld): BlockifierStateOld
    {
        if (c == '}')
        {
            state.currentType = AreaType.Unknown
            if (debug)
                DotlinLogger.log("  -> ${state.currentType}")

            val innerText = state.currentText.substring(1)
            val innerBlocks = blockify(innerText)
            state.blocks.add(ClassBlock(state.currentClassHeader, innerBlocks)) // dotlin
            state.currentClassHeader = ""
            state.currentText = ""
            return state
        }

        state.currentText += c
        return state
    }

    private fun handleInClassHeader(c: Char, state: BlockifierStateOld): BlockifierStateOld
    {
        if (c == '{')
        {
            state.currentType = AreaType.ClassBody
            if (debug)
                DotlinLogger.log("  -> ${state.currentType}")

            state.currentClassHeader = state.currentText
            state.currentText = ""
        }

        state.currentText += c
        return state
    }

    private fun handleInCurlyBrackets(c: Char, state: BlockifierStateOld): BlockifierStateOld
    {
        if (debug)
            DotlinLogger.log("  handleInCurlyBrackets: ${ToolsOld.toDisplayString2(c)} ${ToolsOld.toDisplayString2(state.currentText)}")

        if (c != '}')
        {
            state.currentText += c
            return state
        }

        state.currentType = AreaType.Unknown
        if (debug)
            DotlinLogger.log("  -> ${state.currentType}")

        state.blocks.add(CurlyBracketBlock(mutableListOf(UnknownBlock(state.currentText.substring(1))))) // dotlin
        state.currentText = ""
        return state
    }

    private fun handleInWhitespace(c: Char, state: BlockifierStateOld): BlockifierStateOld
    {
        if (ToolsOld.isWhitespace(c))
        {
            state.currentText += c
            return state
        }

        state.currentType = AreaType.Unknown
        if (debug)
            DotlinLogger.log("  -> ${state.currentType}")

        if (state.currentText.isNotEmpty())
            state.blocks.add(WhitespaceBlock(state.currentText)) // dotlin

        state.currentText = c.toString()
        return state
    }
}
