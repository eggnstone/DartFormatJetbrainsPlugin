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
        private const val debug = false
    }

    private val blocks = arrayListOf<ISimpleBlock>()
    private var currentAreaType: SimpleAreaType = SimpleAreaType.Unknown
    private var currentCurlyBracketCount: Int = 0
    private var currentText: String = ""

    fun blockify(text: String): List<ISimpleBlock>
    {
        for (c in text)
        {
            if (debug)
                println("'${Tools.toDisplayString(c.toString())}' $currentAreaType \"${Tools.toDisplayString(currentText)}\"")

            when (currentAreaType)
            {
                SimpleAreaType.Instruction -> handleInstructionArea(c)
                SimpleAreaType.Unknown -> handleUnknownArea(c)
                SimpleAreaType.Whitespace -> handleWhitespaceArea(c)
            }
        }

        if (currentText.isNotEmpty())
        {
            blocks += when (currentAreaType)
            {
                SimpleAreaType.Instruction -> SimpleInstructionBlock(currentText)
                SimpleAreaType.Unknown -> throw DartFormatException("Unexpected area type: $currentAreaType")
                SimpleAreaType.Whitespace -> SimpleWhitespaceBlock(currentText)
            }
        }

        return blocks
    }

    private fun handleInstructionArea(c: Char)
    {
        if (c == ';')
        {
            blocks += SimpleInstructionBlock(currentText + c)
            reset(SimpleAreaType.Unknown, "")
            return
        }

        if (c == '{')
        {
            currentCurlyBracketCount++
            currentText += c
            return
        }

        if (c == '}')
        {
            currentCurlyBracketCount--

            if (currentCurlyBracketCount < 0)
                throw DartFormatException("currentCurlyBracketCount < 0")

            if (currentCurlyBracketCount == 0)
            {
                blocks += SimpleInstructionBlock(currentText + c)
                reset(SimpleAreaType.Unknown, "")
                return
            }

            currentText += c
            return
        }

        currentText += c
    }

    private fun handleUnknownArea(c: Char)
    {
        if (Tools.isWhitespace(c))
        {
            reset(SimpleAreaType.Whitespace, c.toString())
            return
        }

        reset(SimpleAreaType.Instruction, c.toString())
        if (c == '{')
            currentCurlyBracketCount++
    }

    private fun handleWhitespaceArea(c: Char)
    {
        if (Tools.isWhitespace(c))
        {
            currentText += c
            return
        }

        blocks += SimpleWhitespaceBlock(currentText)
        reset(SimpleAreaType.Instruction, c.toString())
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

    private fun reset(areaType: SimpleAreaType, text: String)
    {
        currentAreaType = areaType
        currentCurlyBracketCount = 0
        this.currentText = text
    }
}
