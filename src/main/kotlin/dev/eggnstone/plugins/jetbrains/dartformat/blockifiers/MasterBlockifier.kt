package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class MasterBlockifier
{
    fun blockify(inputText: String): BlockifyResult
    {
        println("MasterBlockifier.blockify: ${Tools.shorten(inputText, 100)}")

        val parts = mutableListOf<IPart>()

        var remainingText = inputText
        while (remainingText.isNotEmpty())
        {
            val blockifier = getBlockifier(remainingText)
            @Suppress("FoldInitializerAndIfToElvis")
            if (blockifier == null)
                return BlockifyResult(remainingText, parts)

            val result = blockifier.blockify(remainingText)
            remainingText = result.remainingText
            parts += result.parts
        }

        return BlockifyResult("", parts)
    }

    fun getBlockifier(inputText: String): IBlockifier?
    {
        if (inputText.isEmpty())
            throw DartFormatException("Unexpected empty text.")

        @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
        val c = inputText.get(0).toString() // workaround for dotlin for: for (c in text)

        if (Tools.isWhitespace(c))
            return WhitespaceBlockifier()

        if (c == "}")
            return null

        return InstructionBlockifier()
    }
}
