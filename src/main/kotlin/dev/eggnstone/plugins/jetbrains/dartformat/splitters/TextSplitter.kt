package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
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

        var header = ""
        var parts1 = listOf<IPart>()
        var currentMiddle = ""
        var currentText = ""
        var isDoubleBlock = false
        //val parts = mutableListOf<IPart>()

        var remainingText = inputText
        while (remainingText.isNotEmpty())
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val c = remainingText.get(0).toString() // workaround for dotlin for: for (c in text)
            //println("c: $c")

            if (c == ";")
            {
                currentText += c
                remainingText = remainingText.substring(1)

                // TODO: whitespace after else
                if (!remainingText.trim().startsWith("else "))
                    return SplitResult(remainingText, listOf(Statement(currentText)))

                DotlinLogger.log("Expecting 'else' branch.")
                println("currentText:   ${Tools.toDisplayString2(currentText)}")
                println("remainingText: ${Tools.toDisplayString2(remainingText)}")
                continue
            }

            if (c == "{")
            {
                currentText += c
                remainingText = remainingText.substring(1)

                val result = MasterSplitter().split(remainingText)
                remainingText = result.remainingText

                if (!result.remainingText.startsWith("}"))
                    TODO("error") // throw DartFormatException("Unexpected TODO")

                //parts += SingleBlock(currentText, "}", result.parts)

                if (isDoubleBlock)
                {
                    if (remainingText == "}")
                        return SplitResult("", listOf(DoubleBlock(header, currentText, "}", parts1, result.parts)))

                    TODO("error")
                }

                if (remainingText == "}")
                    return SplitResult("", listOf(SingleBlock(currentText, "}", result.parts)))

                // TODO: whitespace after else
                if (DotlinTools.substring(remainingText, 1).trim().startsWith("else "))
                {
                    DotlinLogger.log("Expecting 'else' branch.")
                    isDoubleBlock = true
                    header = currentText
                    parts1 = result.parts
                    currentText = ""
                    println("currentHeader:   ${Tools.toDisplayString2(header)}")
                    println("remainingText: ${Tools.toDisplayString2(remainingText)}")
                    continue
                }

                TODO("error")
            }

            currentText += c
            remainingText = remainingText.substring(1)
        }

        throw DartFormatException("Unexpected end of block or statement.")
    }
}
