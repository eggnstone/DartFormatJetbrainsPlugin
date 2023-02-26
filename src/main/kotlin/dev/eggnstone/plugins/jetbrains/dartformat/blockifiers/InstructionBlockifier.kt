package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Block
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement

class InstructionBlockifier : IBlockifier
{
    override fun blockify(inputText2: String): BlockifyResult
    {
        println("InstructionBlockifier.blockify: ${Tools.shorten(inputText2, 100)}")

        if (inputText2.isEmpty())
            throw DartFormatException("Unexpected empty text.")

        val parts = mutableListOf<IPart>()
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
                return BlockifyResult(resultRemainingText, listOf(Statement(currentText)))
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

                parts += Block("{", "}", result.parts)

                println("- Called MasterBlockifier.")
                println("header:        ${Tools.toDisplayString2(currentText)}")
                println("remainingText: ${Tools.toDisplayString2(remainingText)}")

                if (remainingText == "}")
                    return BlockifyResult("", parts)

                TODO()
                continue
            }

            currentText += c
            remainingText = remainingText.substring(1)
        }

        throw DartFormatException("Unexpected end of instruction.")
    }
}
