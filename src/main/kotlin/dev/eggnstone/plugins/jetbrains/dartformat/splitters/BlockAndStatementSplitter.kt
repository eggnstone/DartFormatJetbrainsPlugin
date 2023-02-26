package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Block
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement

class BlockAndStatementSplitter : ISplitter
{
    override fun split(inputText: String): SplitResult
    {
        println("BlockAndStatementSplitter.split: ${Tools.shorten(inputText, 100)}")

        if (inputText.isEmpty())
            throw DartFormatException("Unexpected empty text.")

        val parts = mutableListOf<IPart>()
        var currentText = ""

        var remainingText = inputText
        while (remainingText.isNotEmpty())
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val c = remainingText.get(0).toString() // workaround for dotlin for: for (c in text)
            println("c: $c")

            if (c == ";")
            {
                currentText += c
                val resultRemainingText = remainingText.substring(1)
                return SplitResult(resultRemainingText, listOf(Statement(currentText)))
            }

            if (c == "{")
            {
                currentText += c
                val tempRemainingText = remainingText.substring(1)

                println("- Calling Splitter ...")
                val result = Splitter().split(tempRemainingText)
                remainingText = result.remainingText

                if (!result.remainingText.startsWith("}"))
                    TODO() // throw

                parts += Block("{", "}", result.parts)

                println("- Called Splitter.")
                println("header:        ${Tools.toDisplayString2(currentText)}")
                println("remainingText: ${Tools.toDisplayString2(remainingText)}")

                if (remainingText == "}")
                    return SplitResult("", parts)

                TODO()
                continue
            }

            currentText += c
            remainingText = remainingText.substring(1)
        }

        throw DartFormatException("Unexpected end of block or statement.")
    }
}
