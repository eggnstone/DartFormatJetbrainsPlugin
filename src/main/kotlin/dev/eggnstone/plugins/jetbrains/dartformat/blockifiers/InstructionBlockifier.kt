package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.BlockInstructionBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.PlainInstructionBlock

class InstructionBlockifier : IBlockifier
{
    override fun blockify(inputText2: String): BlockifyResult
    {
        println("InstructionBlockifier.blockify: ${Tools.shorten(inputText2, 100)}")

        if (inputText2.isEmpty())
            throw DartFormatException("Unexpected empty text.")

        val blocks = mutableListOf<IBlock>()
        var currentText = ""

        var remainingText = inputText2
        while (remainingText.isNotEmpty())
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val c = remainingText.get(0).toString() // workaround for dotlin for: for (c in text)
            println("c: $c")

            if (c == ";")
            {
                currentText += c
                val resultRemainingText = remainingText.substring(1)
                return BlockifyResult(resultRemainingText, listOf(PlainInstructionBlock(currentText)))
            }

            if (c == "{")
            {
                currentText += c
                val tempRemainingText = remainingText.substring(1)

                println("- Calling MasterBlockifier ...")
                val result = MasterBlockifier().blockify(tempRemainingText)
                remainingText = result.remainingText

                if (!result.remainingText.startsWith("}"))
                    TODO() // throw

                blocks += BlockInstructionBlock("{", "}", result.blocks)

                println("- Called MasterBlockifier.")
                println("header:        ${Tools.toDisplayString2(currentText)}")
                println("remainingText: ${Tools.toDisplayString2(remainingText)}")

                if (remainingText == "}")
                    return BlockifyResult("", blocks)

                TODO()
                continue
            }

            currentText += c
            remainingText = remainingText.substring(1)
        }

        throw DartFormatException("Unexpected end of instruction.")
    }
}
