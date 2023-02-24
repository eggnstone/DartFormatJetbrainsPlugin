package com.eggnstone.jetbrainsplugins.dartformat.blockifier

import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.blocks.*

class Blockifier
{
    companion object
    {
        const val debug = false
    }

    fun printBlocks(blocks: List<IBlock>)
    {
        for (block in blocks)
        {
            println("Block: ${block::class.simpleName}")
            println("  $block")
        }
    }

    fun blockify(text: String): List<IBlock>
    {
        var state = BlockifierState()

        for (c in text)
        {
            if (debug)
                println("'${Tools.toDisplayString(c.toString())}' ${state.currentType} \"${Tools.toDisplayString(state.currentText)}\"")

            if (state.currentType != BlockType.Unknown)
            {
                state = when (state.currentType)
                {
                    BlockType.ClassBody -> handleInClassBody(c, state)
                    BlockType.ClassHeader -> handleInClassHeader(c, state)
                    BlockType.CurlyBrackets -> handleInCurlyBrackets(c, state)
                    BlockType.Whitespace -> handleInWhitespace(c, state)
                    else -> throw DartFormatException("Unhandled BlockType: ${state.currentType}")
                }
                continue
            }

            if (Tools.isWhitespace(c))
            {
                if (state.currentText.isEmpty())
                {
                    state.currentType = BlockType.Whitespace
                    if (debug)
                        println("  -> ${state.currentType}")

                    state.currentText += c
                    continue
                }

                if (state.currentText == "class" || state.currentText == "abstract class")
                {
                    state.currentType = BlockType.ClassHeader
                    if (debug)
                        println("  -> ${state.currentType}")
                }

                state.currentText += c
                continue
            }

            if (c == '{')
            {
                if (state.currentText.isEmpty())
                {
                    state.currentType = BlockType.CurlyBrackets
                    if (debug)
                        println("  -> ${state.currentType}")

                    state.currentText += c
                    continue
                }
            }

            if (c == ';')
            {
                state.blocks += ExpressionBlock(state.currentText + c)
                state.currentText = ""
                continue
            }

            state.currentText += c
        }

        if (state.currentText.isNotEmpty())
        {
            if (state.currentType == BlockType.Whitespace)
                state.blocks += WhitespaceBlock(state.currentText)
            else
                throw DartFormatException("Unhandled BlockType at end of text: ${state.currentType}")
        }

        return state.blocks
    }

    private fun handleInClassBody(c: Char, state: BlockifierState): BlockifierState
    {
        if (c == '}')
        {
            state.currentType = BlockType.Unknown
            if (debug)
                println("  -> ${state.currentType}")

            val innerText = state.currentText.substring(1)
            val innerBlocks = blockify(innerText)
            state.blocks += ClassBlock(state.currentClassHeader, innerBlocks)
            state.currentClassHeader = ""
            state.currentText = ""
            return state
        }

        state.currentText += c
        return state
    }

    private fun handleInClassHeader(c: Char, state: BlockifierState): BlockifierState
    {
        if (c == '{')
        {
            state.currentType = BlockType.ClassBody
            if (debug)
                println("  -> ${state.currentType}")

            state.currentClassHeader = state.currentText
            state.currentText = ""
        }

        state.currentText += c
        return state
    }

    private fun handleInCurlyBrackets(c: Char, state: BlockifierState): BlockifierState
    {
        if (debug)
            println("  handleInCurlyBrackets: '${Tools.toDisplayString(c.toString())}' \"${Tools.toDisplayString(state.currentText)}\"")

        if (c != '}')
        {
            state.currentText += c
            return state
        }

        state.currentType = BlockType.Unknown
        if (debug)
            println("  -> ${state.currentType}")

        state.blocks += CurlyBracketsBlock(state.currentText.substring(1))
        state.currentText = ""
        return state
    }

    private fun handleInWhitespace(c: Char, state: BlockifierState): BlockifierState
    {
        if (Tools.isWhitespace(c))
        {
            state.currentText += c
            return state
        }

        state.currentType = BlockType.Unknown
        if (debug)
            println("  -> ${state.currentType}")

        if (state.currentText.isNotEmpty())
            state.blocks += WhitespaceBlock(state.currentText)

        state.currentText = c.toString()
        return state
    }
}
