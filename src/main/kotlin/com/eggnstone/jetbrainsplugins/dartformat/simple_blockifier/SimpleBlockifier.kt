package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.blocks.ISimpleBlock
import com.eggnstone.jetbrainsplugins.dartformat.blocks.SimpleInstructionBlock
import com.eggnstone.jetbrainsplugins.dartformat.blocks.SimpleWhitespaceBlock

class SimpleBlockifier
{
    companion object
    {
        const val debug = false
    }

    fun blockify(text: String): List<ISimpleBlock>
    {
        var state = SimpleBlockifierState()

        for (c in text)
        {
            if (debug)
                println("'${Tools.toDisplayString(c.toString())}' ${state.currentAreaType} \"${Tools.toDisplayString(state.currentText)}\"")

            state = when (state.currentAreaType)
            {
                SimpleAreaType.Instruction -> handleInstructionArea(c, state)
                SimpleAreaType.Unknown -> handleUnknownArea(c, state)
                SimpleAreaType.Whitespace -> handleWhitespaceArea(c, state)
            }
        }

        if (state.currentText.isNotEmpty())
        {
            state.blocks += when (state.currentAreaType)
            {
                SimpleAreaType.Instruction -> SimpleInstructionBlock(state.currentText)
                SimpleAreaType.Unknown -> throw DartFormatException("Unexpected area type: ${state.currentAreaType}")
                SimpleAreaType.Whitespace -> SimpleWhitespaceBlock(state.currentText)
            }
        }

        return state.blocks
    }

    private fun handleInstructionArea(c: Char, state: SimpleBlockifierState): SimpleBlockifierState
    {
        if (c == ';')
        {
            state.blocks += SimpleInstructionBlock(state.currentText + c)
            state.reset(SimpleAreaType.Unknown, "")
            return state
        }

        if (c == '{')
        {
            state.currentCurlyBracketCount++
            state.currentText += c
            return state
        }

        if (c == '}')
        {
            state.currentCurlyBracketCount--

            if (state.currentCurlyBracketCount < 0)
                throw DartFormatException("state.currentCurlyBracketCount < 0")

            if (state.currentCurlyBracketCount == 0)
            {
                state.blocks += SimpleInstructionBlock(state.currentText + c)
                state.reset(SimpleAreaType.Unknown, "")
                return state
            }

            state.currentText += c
            return state
        }

        state.currentText += c
        return state
    }

    private fun handleUnknownArea(c: Char, state: SimpleBlockifierState): SimpleBlockifierState
    {
        if (Tools.isWhitespace(c))
        {
            state.reset(SimpleAreaType.Whitespace, c.toString())
            return state
        }

        state.reset(SimpleAreaType.Instruction, c.toString())
        if (c == '{')
            state.currentCurlyBracketCount++

        return state
    }

    private fun handleWhitespaceArea(c: Char, state: SimpleBlockifierState): SimpleBlockifierState
    {
        if (Tools.isWhitespace(c))
        {
            state.currentText += c
            return state
        }

        state.blocks += SimpleWhitespaceBlock(state.currentText)
        state.reset(SimpleAreaType.Instruction, c.toString())
        return state
    }

    fun printBlocks(blocks: List<ISimpleBlock>)
    {
        if (blocks.isEmpty())
            println("No blocks.")
        else
            println("${blocks.size} blocks:")

        for (block in blocks)
            println("  $block")
    }
}
