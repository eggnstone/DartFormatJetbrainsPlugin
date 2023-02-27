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
        //DotlinLogger.log("TextSplitter.split: ${Tools.shorten(inputText, 100)}")

        if (inputText.isEmpty())
            throw DartFormatException("Unexpected empty text.")

        var header = ""
        var parts1 = listOf<IPart>()
        val currentBrackets = mutableListOf<String>()
        var currentMiddle = ""
        var currentText = ""
        var isDoubleBlock = false
        //val parts = mutableListOf<IPart>()

        var remainingText = inputText
        while (remainingText.isNotEmpty())
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val c = remainingText.get(0).toString() // workaround for dotlin for: for (c in text)
            //DotlinLogger.log("c: $c")

            if (c == ";")
            {
                currentText += c
                remainingText = remainingText.substring(1)

                if (remainingText.trim().startsWith("else"))
                {
                    if (remainingText.trim().startsWith("else "))
                    {
                        DotlinLogger.log("Expecting 'else' branch.")
                        DotlinLogger.log("currentText:   ${Tools.toDisplayString2(currentText)}")
                        DotlinLogger.log("remainingText: ${Tools.toDisplayString2(remainingText)}")
                        continue
                    }

                    TODO("whitespace after else")
                }

                return SplitResult(remainingText, listOf(Statement(currentText)))
            }

            if (c == "{" && currentBrackets.isEmpty())
            {
                currentText += c
                remainingText = remainingText.substring(1)

                val result = MasterSplitter().split(remainingText)
                remainingText = result.remainingText

                if (!remainingText.startsWith("}"))
                    TODO("error")

                if (isDoubleBlock)
                {
                    if (remainingText == "}")
                        return SplitResult("", listOf(DoubleBlock(header, currentText, "}", parts1, result.parts)))

                    DotlinLogger.log("currentText:   ${Tools.toDisplayString2(currentText)}")
                    DotlinLogger.log("currentHeader: ${Tools.toDisplayString2(header)}")
                    DotlinLogger.log("remainingText: ${Tools.toDisplayString2(remainingText)}")
                    TODO("error")
                }

                if (remainingText == "}")
                    return SplitResult("", listOf(SingleBlock(currentText, "}", result.parts)))

                if (DotlinTools.substring(remainingText, 1).trim().startsWith("else"))
                {
                    if (DotlinTools.substring(remainingText, 1).trim().startsWith("else "))
                    {
                        DotlinLogger.log("Expecting 'else' branch.")
                        DotlinLogger.log("c:               ${Tools.toDisplayString(c)}")
                        DotlinLogger.log("currentBrackets: ${Tools.stringsToDisplayString2(currentBrackets)}")
                        DotlinLogger.log("currentText:     ${Tools.toDisplayString2(currentText)}")
                        DotlinLogger.log("remainingText:   ${Tools.toDisplayString2(remainingText)}")
                        DotlinLogger.log("result.parts:    ${Tools.partsToDisplayString2(result.parts)}")
                        isDoubleBlock = true
                        header = currentText
                        parts1 = result.parts
                        currentText = "}"
                        remainingText = DotlinTools.substring(remainingText, 1)
                        continue
                    }

                    TODO("whitespace after else")
                }

                DotlinLogger.log("currentText:   ${Tools.toDisplayString2(currentText)}")
                DotlinLogger.log("currentHeader: ${Tools.toDisplayString2(header)}")
                DotlinLogger.log("remainingText: ${Tools.toDisplayString2(remainingText)}")
                TODO("error")
            }

            if (Tools.isOpeningBracket(c))
                currentBrackets.add(c)

            if (Tools.isClosingBracket(c))
            {
                if (currentBrackets.isEmpty())
                {
                    DotlinLogger.log("currentBrackets: ${Tools.stringsToDisplayString2(currentBrackets)}")
                    DotlinLogger.log("currentHeader:   ${Tools.toDisplayString2(header)}")
                    DotlinLogger.log("currentText:     ${Tools.toDisplayString2(currentText)}")
                    DotlinLogger.log("remainingText:   ${Tools.toDisplayString2(remainingText)}")
                    throw DartFormatException("Unexpected closing curly bracket.")
                }

                val lastOpeningBracket = currentBrackets.removeLast()
                val expectedClosingBracket = Tools.getClosingBracket(lastOpeningBracket)
                if (c != expectedClosingBracket)
                    TODO("error")
            }

            currentText += c
            remainingText = remainingText.substring(1)
        }

        DotlinLogger.log("currentText:   ${Tools.toDisplayString2(currentText)}")
        DotlinLogger.log("currentHeader: ${Tools.toDisplayString2(header)}")
        DotlinLogger.log("remainingText: ${Tools.toDisplayString2(remainingText)}")
        throw DartFormatException("Unexpected end of block or statement.")
    }
}
