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

    fun printBlocks(blocks: List<ISimpleBlock>)
    {
        if (blocks.isEmpty())
            println("No blocks.")
        else
            println("${blocks.size} blocks:")

        for (block in blocks)
            println("  $block")
    }

    fun blockify(text: String): List<ISimpleBlock>
    {
        var state = SimpleBlockifierState()

        for (c in text)
        {
            if (debug)
                println("'${Tools.toDisplayString(c.toString())}' ${state.currentAreaType} \"${Tools.toDisplayString(state.currentText)}\"")

            if (state.currentAreaType == SimpleAreaType.Instruction)
            {
                if (c == ';')
                {
                    state.blocks += SimpleInstructionBlock(state.currentText + c)
                    state.currentAreaType = SimpleAreaType.Unknown
                    state.currentText = ""
                    continue
                }

                state.currentText += c
                continue
            }

            if (state.currentAreaType == SimpleAreaType.Whitespace)
            {
                if (Tools.isWhitespace(c))
                {
                    state.currentText += c
                    continue
                }

                state.blocks += SimpleWhitespaceBlock(state.currentText)
                state.currentAreaType = SimpleAreaType.Unknown
                state.currentText = c.toString()
                continue
            }

            if (Tools.isWhitespace(c))
            {
                state.currentAreaType = SimpleAreaType.Whitespace
                state.currentText += c
                continue
            }

            state.currentAreaType = SimpleAreaType.Instruction
            state.currentText += c
        }

        if (state.currentText.isNotEmpty())
        {
            when (state.currentAreaType)
            {
                SimpleAreaType.Instruction -> state.blocks += SimpleInstructionBlock(state.currentText)
                SimpleAreaType.Unknown -> throw DartFormatException("Unexpected area type: ${state.currentAreaType}")
                SimpleAreaType.Whitespace -> state.blocks += SimpleWhitespaceBlock(state.currentText)
            }
        }

        return state.blocks
    }
}
