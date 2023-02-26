package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.InstructionBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.WhitespaceBlock
import dev.eggnstone.plugins.jetbrains.dartformat.simple_blockifier.SimpleAreaType

class Blockifier
{
    private val blocks = mutableListOf<IBlock>()
    private var currentAreaType: SimpleAreaType = SimpleAreaType.Unknown
    private var currentText: String = ""

    fun blockify(text: String): List<IBlock>
    {
        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until text.length) // workaround for dotlin for: for (c in text)
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val c = text.get(i).toString() // workaround for dotlin for: for (c in text)

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
                SimpleAreaType.Instruction -> InstructionBlock(currentText, "", listOf())
                SimpleAreaType.Unknown -> TODO()
                SimpleAreaType.Whitespace -> WhitespaceBlock(currentText)
            }
        }

        return blocks
    }

    private fun handleInstructionArea(c: String)
    {
        currentText += c
    }

    private fun handleUnknownArea(c: String)
    {
        if (Tools.isWhitespace(c))
        {
            reset(SimpleAreaType.Whitespace, c)
            return
        }

        if (c == "{")
        {
            reset(SimpleAreaType.Instruction, c)
            return
        }

        TODO()
    }

    private fun handleWhitespaceArea(c: String)
    {
        if (Tools.isWhitespace(c))
        {
            currentText += c
            return
        }

        TODO()

        blocks.add(WhitespaceBlock(currentText)) // workaround for dotlin for: +=
        reset(SimpleAreaType.Instruction, c)
    }

    private fun reset(areaType: SimpleAreaType, text: String)
    {
        currentAreaType = areaType
        //currentBrackets.clear()
        currentText = text
        //hasMainCurlyBrackets = false
    }
}