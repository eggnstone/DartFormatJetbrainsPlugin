package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.dotlin.DotlinChar
import com.eggnstone.jetbrainsplugins.dartformat.dotlin.DotlinLogger
import com.eggnstone.jetbrainsplugins.dartformat.simple_blocks.ISimpleBlock
import com.eggnstone.jetbrainsplugins.dartformat.simple_blocks.SimpleInstructionBlock
import com.eggnstone.jetbrainsplugins.dartformat.simple_blocks.SimpleWhitespaceBlock

class SimpleBlockifier
{
    companion object
    {
        private const val debug = false
    }

    private val blocks = mutableListOf<ISimpleBlock>()
    private var currentAreaType: SimpleAreaType = SimpleAreaType.Unknown
    private val currentBrackets = mutableListOf<DotlinChar>()
    private var currentText: String = ""
    private var hasMainCurlyBrackets = false

    fun blockify(text: String): List<ISimpleBlock>
    {
        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until text.length)
        {
            // dotlin
            @Suppress("ReplaceGetOrSet")
            val c = DotlinChar(text.get(i).toString())

            if (debug)
                DotlinLogger.log("${Tools.toDisplayString2(c)} $currentAreaType ${Tools.toDisplayString2(currentText)}")

            when (currentAreaType)
            {
                SimpleAreaType.Instruction -> handleInstructionArea(c)
                SimpleAreaType.Unknown -> handleUnknownArea(c)
                SimpleAreaType.Whitespace -> handleWhitespaceArea(c)
                else -> throwError("only necessary because of dotlin")
            }
        }

        @Suppress("ReplaceSizeCheckWithIsNotEmpty") // dotlin
        if (currentText.length > 0)
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
                DotlinLogger.log("Final block: $finalBlock")

            blocks.add(finalBlock) // dotlin
        }
        else
        {
            if (debug)
                printBlocks(blocks, "Final / currentText is empty")
        }

        return blocks
    }

    private fun handleInstructionArea(c: DotlinChar)
    {
        if (c.value == ";" && currentBrackets.size == 0)
        {
            val dotlinC: String = c.value // dotlin
            blocks.add(SimpleInstructionBlock(currentText + dotlinC))
            reset(SimpleAreaType.Unknown, "")
            return
        }

        if (Tools.isOpeningBracket(c))
        {
            //DotlinTools.println("TODO? $currentBrackets")
            if (c.value == "{" && currentBrackets.size == 0)
                hasMainCurlyBrackets = true

            currentBrackets.add(c) // dotlin
            val dotlinC: String = c.value // dotlin
            currentText += dotlinC
            return
        }

        if (Tools.isClosingBracket(c))
        {
            val lastOrNull = if (currentBrackets.size == 0) null else currentBrackets[currentBrackets.size - 1]
            if (lastOrNull != Tools.getOpeningBracket(c))
                throwError("Unexpected closing bracket.")

            currentBrackets.removeLast()

            if (currentBrackets.size == 0)
            {
                if (hasMainCurlyBrackets)
                {
                    blocks.add(SimpleInstructionBlock(currentText + c)) // dotlin
                    reset(SimpleAreaType.Unknown, "")
                    return
                }

                /*blocks += SimpleInstructionBlock(currentText + c)
                reset(SimpleAreaType.Unknown, "")
                return*/
            }

            val dotlinC: String = c.value // dotlin
            currentText += dotlinC
            return
        }

        val dotlinC: String = c.value // dotlin
        currentText += dotlinC
    }

    private fun handleUnknownArea(c: DotlinChar)
    {
        if (Tools.isWhitespace(c))
        {
            reset(SimpleAreaType.Whitespace, c.value)
            return
        }

        reset(SimpleAreaType.Instruction, c.value)
        if (Tools.isOpeningBracket(c))
        {
            currentBrackets.add(c) // dotlin
            if (c.value == "{")
                hasMainCurlyBrackets = true
        }
    }

    private fun handleWhitespaceArea(c: DotlinChar)
    {
        if (Tools.isWhitespace(c))
        {
            val dotlinC: String = c.value // dotlin
            currentText += dotlinC
            return
        }

        blocks.add(SimpleWhitespaceBlock(currentText)) // dotlin
        reset(SimpleAreaType.Instruction, c.value)
    }

    fun printBlocks(blocks: List<ISimpleBlock>, label: String = "")
    {
        @Suppress("ReplaceSizeZeroCheckWithIsEmpty") // dotlin
        val prefix = if (label.length == 0) "" else "$label - "

        @Suppress("ReplaceSizeZeroCheckWithIsEmpty") // dotlin
        if (blocks.size == 0)
            DotlinLogger.log("${prefix}No blocks.")
        else
            DotlinLogger.log("$prefix${blocks.size} blocks:")

        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until blocks.size)
        {
            val block = blocks[i]
            DotlinLogger.log("  $block")
        }

        /* dotlin
        for (block in blocks)
            DotlinLogger.log("  $block")
        */
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
        DotlinLogger.log("Error: $message")
        DotlinLogger.log("  currentAreaType:      $currentAreaType")
        DotlinLogger.log("  currentBrackets:      ${Tools.charsToDisplayString2(currentBrackets)}")
        DotlinLogger.log("  currentText:          ${Tools.toDisplayString2(currentText)}")
        DotlinLogger.log("  hasMainCurlyBrackets: $hasMainCurlyBrackets")
        printBlocks(blocks)
        throw DartFormatException(message)
    }
}
