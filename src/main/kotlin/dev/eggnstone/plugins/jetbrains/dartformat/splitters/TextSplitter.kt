package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement

class TextSplitter : ISplitter
{
    override fun split(inputText: String): SplitResult
    {
        //println("TextSplitter.split: ${Tools.shorten(inputText, 100)}")

        if (inputText.isEmpty())
            throw DartFormatException("Unexpected empty text.")

        val parts = mutableListOf<IPart>()
        var currentText = ""

        var remainingText = inputText
        while (remainingText.isNotEmpty())
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val c = remainingText.get(0).toString() // workaround for dotlin for: for (c in text)
            //println("c: $c")

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
                val result = MasterSplitter().split(tempRemainingText)
                remainingText = result.remainingText

                if (!result.remainingText.startsWith("}"))
                    TODO() // throw DartFormatException("Unexpected TODO")

                parts += SingleBlock(currentText, "}", result.parts)

                if (remainingText == "}")
                    return SplitResult("", parts)

                println("currentText:   ${Tools.toDisplayString2(currentText)}")
                println("remainingText: ${Tools.toDisplayString2(remainingText)}")

                TODO()
                continue
            }

            currentText += c
            remainingText = remainingText.substring(1)
        }

        throw DartFormatException("Unexpected end of block or statement.")
    }
}
