package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.InstructionBlock

class InstructionBlockifier : IBlockifier
{
    override fun blockify(inputText: String): BlockifyResult
    {
        println("InstructionBlockifier.blockify: ${Tools.shorten(inputText, 100)}")

        if (inputText.isEmpty())
            throw DartFormatException("Unexpected empty text.")

        var header = ""

        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until inputText.length) // workaround for dotlin for: for (c in text)
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val c = inputText.get(i).toString() // workaround for dotlin for: for (c in text)

            if (c == ";")
            {
                header += c
                val remainingText = inputText.substring(i + 1)
                return BlockifyResult(remainingText, listOf(InstructionBlock(header, "")))
            }

            if (c == "{")
            {
                header += c
                val remainingText = inputText.substring(i + 1)
                val result = MasterBlockifier().blockify(remainingText)
                //remainingText = result.remainingText
                //blocks
                TODO()
                return BlockifyResult("", listOf(InstructionBlock(inputText, "", result.blocks)))
            }

            header += c
            continue
        }

        throw DartFormatException("Unexpected end of instruction.")
        TODO()
        return BlockifyResult("", listOf(InstructionBlock(inputText, "")))
    }
}
