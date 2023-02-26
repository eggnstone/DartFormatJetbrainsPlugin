package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.Tools
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
    private val currentBrackets = mutableListOf<String>()
    private var currentText: String = ""
    private var hasMainCurlyBrackets = false

    fun blockify(text: String): List<ISimpleBlock>
    {
        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until text.length) // workaround for dotlin for: for (c in text)
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val c = text.get(i).toString() // workaround for dotlin for: for (c in text)

            if (debug)
                DotlinLogger.log("${Tools.toDisplayString2(c)} $currentAreaType ${Tools.toDisplayString2(currentText)}")

            when (currentAreaType)
            {
                SimpleAreaType.Instruction -> handleInstructionArea(c)
                SimpleAreaType.Unknown -> handleUnknownArea(c)
                SimpleAreaType.Whitespace -> handleWhitespaceArea(c)
                else -> throwError("only necessary because of dotlin") // workaround for dotlin for: else missing
            }
        }

        @Suppress("ReplaceSizeCheckWithIsNotEmpty") // workaround for dotlin for: isEmpty
        if (currentText.length > 0) // dotlin
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
                /*else if (hasMainCurlyBrackets)
                    finalBlock = SimpleInstructionBlock(currentText) // TODO: necessary or not? No test covers this. */
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

            blocks.add(finalBlock) // workaround for dotlin for: +=
        }
        else
        {
            if (debug)
                printBlocks(blocks, "Final / currentText is empty")
        }

        return blocks
    }

    private fun handleInstructionArea(c: String)
    {
        if (c == ";" && currentBrackets.size == 0)
        {
            blocks.add(SimpleInstructionBlock(currentText + c))
            reset(SimpleAreaType.Unknown, "")
            return
        }

        if (Tools.isOpeningBracket(c))
        {
            if (c == "{" && currentBrackets.size == 0)
                hasMainCurlyBrackets = true

            currentBrackets.add(c) // workaround for dotlin for: += c
            currentText += c // workaround for dotlin for: += c
            return
        }

        if (Tools.isClosingBracket(c))
        {
            val lastOrNull = if (currentBrackets.size == 0) null else currentBrackets[currentBrackets.size - 1]
            if (lastOrNull != Tools.getOpeningBracket(c))
                throwError("Unexpected closing bracket.")

            currentBrackets.removeLast()

            if (currentBrackets.size == 0) // workaround for dotlin for: isEmpty
            {
                if (hasMainCurlyBrackets)
                {
                    blocks.add(SimpleInstructionBlock(currentText + c)) // workaround for dotlin for: +=
                    reset(SimpleAreaType.Unknown, "")
                    return
                }

                /*blocks += SimpleInstructionBlock(currentText + c)
                reset(SimpleAreaType.Unknown, "")
                return*/
            }

            currentText += c // workaround for dotlin for: += c
            return
        }

        currentText += c // workaround for dotlin for: += c
    }

    private fun handleUnknownArea(c: String)
    {
        if (Tools.isWhitespace(c))
        {
            reset(SimpleAreaType.Whitespace, c)
            return
        }

        reset(SimpleAreaType.Instruction, c)
        if (Tools.isOpeningBracket(c))
        {
            currentBrackets.add(c) // workaround for dotlin for: += c
            if (c == "{")
                hasMainCurlyBrackets = true
        }
    }

    private fun handleWhitespaceArea(c: String)
    {
        if (Tools.isWhitespace(c))
        {
            val dotlinC: String = c // workaround for dotlin for: += c
            currentText += dotlinC // workaround for dotlin for: += c
            return
        }

        blocks.add(SimpleWhitespaceBlock(currentText)) // workaround for dotlin for: +=
        reset(SimpleAreaType.Instruction, c)
    }

    fun printBlocks(blocks: List<ISimpleBlock>, label: String = "")
    {
        @Suppress("ReplaceSizeZeroCheckWithIsEmpty") // workaround for dotlin for: isEmpty
        val prefix = if (label.length == 0) "" else "$label - "

        @Suppress("ReplaceSizeZeroCheckWithIsEmpty") // workaround for dotlin for: isEmpty
        if (blocks.size == 0)
            DotlinLogger.log("${prefix}No blocks.")
        else
            DotlinLogger.log("$prefix${blocks.size} blocks:")

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin for: for (block in blocks)
        for (i in 0 until blocks.size) // workaround for dotlin for: for (block in blocks)
        {
            val block = blocks[i] // workaround for dotlin for: for (block in blocks)
            DotlinLogger.log("  $block")
        }
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
        DotlinLogger.log("  currentBrackets:      ${Tools.stringsToDisplayString2(currentBrackets)}")
        DotlinLogger.log("  currentText:          ${Tools.toDisplayString2(currentText)}")
        DotlinLogger.log("  hasMainCurlyBrackets: $hasMainCurlyBrackets")
        printBlocks(blocks)
        throw DartFormatException(message)
    }
}
