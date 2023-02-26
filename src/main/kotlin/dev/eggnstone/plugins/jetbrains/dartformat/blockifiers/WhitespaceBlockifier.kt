package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.WhitespaceBlock

class WhitespaceBlockifier
{
    fun blockify(inputText: String): BlockifyResult
    {
        if (inputText.isEmpty())
            throw DartFormatException("Unexpected empty text.")

        var whitespace = ""

        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until inputText.length) // workaround for dotlin for: for (c in text)
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val c = inputText.get(i).toString() // workaround for dotlin for: for (c in text)

            if (Tools.isWhitespace(c))
            {
                whitespace += c
                continue
            }

            if (i == 0)
                throw DartFormatException("Unexpected non-whitespace at beginning of text.")

            val remainingText = inputText.substring(i)
            return BlockifyResult(remainingText, WhitespaceBlock(whitespace))
        }

        return BlockifyResult("", WhitespaceBlock(inputText))
    }
}
