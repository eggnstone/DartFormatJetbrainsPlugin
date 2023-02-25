package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.simple_blocks.ISimpleBlock
import com.eggnstone.jetbrainsplugins.dartformat.simple_blocks.SimpleInstructionBlock
import com.eggnstone.jetbrainsplugins.dartformat.simple_blocks.SimpleWhitespaceBlock

class SimpleBlockifier
{
    companion object
    {
        private const val debug = false
    }

    private val blocks = arrayListOf<ISimpleBlock>()
    private var currentAreaType: SimpleAreaType = SimpleAreaType.Unknown
    private val currentBrackets = arrayListOf<Char>()
    private var currentText: String = ""
    private var hasMainCurlyBrackets = false

    fun blockify(text: String): List<ISimpleBlock>
    {
        for (c in text)
        {
            if (debug)
                println("${Tools.toDisplayString2(c)} $currentAreaType ${Tools.toDisplayString2(currentText)}")

            when (currentAreaType)
            {
                SimpleAreaType.Instruction -> handleInstructionArea(c)
                SimpleAreaType.Unknown -> handleUnknownArea(c)
                SimpleAreaType.Whitespace -> handleWhitespaceArea(c)
            }
        }

        if (currentText.isNotEmpty())
        {
            var finalBlock: ISimpleBlock? = null

            if (debug)
                printBlocks(blocks, "Final / currentText is not empty")

            if (currentAreaType == SimpleAreaType.Instruction)
            {
                if (currentBrackets.isNotEmpty())
                    throwError("Text ends but brackets not closed.")

                if (currentText == ";")
                    finalBlock = SimpleInstructionBlock(currentText)
                else if (hasMainCurlyBrackets)
                    finalBlock = SimpleInstructionBlock(currentText)
            }

            if (finalBlock == null)
            {
                finalBlock = when (currentAreaType)
                {
                    SimpleAreaType.Instruction -> throwError("Text ends but instruction not closed.")
                    SimpleAreaType.Unknown -> throwError("Unexpected area type: $currentAreaType") // Impossible to cover with tests.
                    SimpleAreaType.Whitespace -> SimpleWhitespaceBlock(currentText)
                }
            }

            if (debug)
                println("Final block: $finalBlock")

            blocks += finalBlock
        }
        else
        {
            if (debug)
                printBlocks(blocks, "Final / currentText is empty")
        }

        return blocks
    }

    private fun handleInstructionArea(c: Char)
    {
        if (c == ';' && currentBrackets.isEmpty())
        {
            blocks += SimpleInstructionBlock(currentText + c)
            reset(SimpleAreaType.Unknown, "")
            return
        }

        if (Tools.isOpeningBracket(c))
        {
            //println("XXXXXXXXXXXXXX: $currentBrackets")
            if (c == '{' && currentBrackets.isEmpty())
                hasMainCurlyBrackets = true

            currentBrackets += c
            currentText += c
            return
        }

        if (Tools.isClosingBracket(c))
        {
            if (currentBrackets.lastOrNull() != Tools.getOpeningBracket(c))
                throwError("Unexpected closing bracket.")

            currentBrackets.removeLast()

            if (currentBrackets.isEmpty())
            {
                if (hasMainCurlyBrackets)
                {
                    blocks += SimpleInstructionBlock(currentText + c)
                    reset(SimpleAreaType.Unknown, "")
                    return
                }

                /*blocks += SimpleInstructionBlock(currentText + c)
                reset(SimpleAreaType.Unknown, "")
                return*/
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
        if (Tools.isOpeningBracket(c))
        {
            currentBrackets += c
            if (c == '{')
                hasMainCurlyBrackets = true
        }
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

    fun printBlocks(blocks: List<ISimpleBlock>, label: String = "")
    {
        val prefix = if (label.isEmpty()) "" else "$label - "

        if (blocks.isEmpty())
            println("${prefix}No blocks.")
        else
            println("$prefix${blocks.size} blocks:")

        for (block in blocks)
            println("  $block")
    }

    private fun reset(areaType: SimpleAreaType, text: String)
    {
        currentAreaType = areaType
        currentBrackets.clear()
        currentText = text
        hasMainCurlyBrackets = false
    }

    private fun throwError(message: String): ISimpleBlock
    {
        println("Error: $message")
        println("  currentAreaType:      $currentAreaType")
        println("  currentBrackets:      ${Tools.charsToDisplayString2(currentBrackets)}")
        println("  currentText:          ${Tools.toDisplayString2(currentText)}")
        println("  hasMainCurlyBrackets: $hasMainCurlyBrackets")
        printBlocks(blocks)
        throw DartFormatException(message)
    }
}
