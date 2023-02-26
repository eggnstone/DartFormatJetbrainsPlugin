package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock

class MasterBlockifier
{
    fun blockify(inputText: String): List<IBlock>
    {
        val blocks = mutableListOf<IBlock>()

        var remainingText = inputText
        while (remainingText.isNotEmpty())
        {
            val blockifier = getBlockifier(remainingText)
            val result = blockifier.blockify(remainingText)
            remainingText = result.remainingText
            blocks += result.block
        }

        return blocks
    }

    fun getBlockifier(inputText: String): IBlockifier
    {
        if (inputText.isEmpty())
            throw DartFormatException("Unexpected empty text.")

        @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
        val c = inputText.get(0).toString() // workaround for dotlin for: for (c in text)

        if (Tools.isWhitespace(c))
            return WhitespaceBlockifier()

        return InstructionBlockifier()
    }
}
