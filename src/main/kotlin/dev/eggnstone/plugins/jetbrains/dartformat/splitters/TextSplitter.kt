package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.extractors.CommentExtractor
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement

class TextSplitter : ISplitter
{
    override fun split(inputText: String): SplitResult
    {
        //DotlinLogger.log("TextSplitter.split: ${Tools.toDisplayString(Tools.shorten(inputText, 100))}")

        if (DotlinTools.isEmpty(inputText))
            throw DartFormatException("Unexpected empty text.")

        var header = ""

        var parts1: List<IPart> = listOf() // ok
        /*//var parts: List<X> = listOf() // ok
        var parts = listOf<X>() // error
        parts = mutableListOf<X>()*/

        val currentBrackets = mutableListOf<String>()
        //var currentMiddle = ""
        var currentText = ""
        var isDoubleBlock = false
        //val parts = mutableListOf<IPart>()

        var remainingText = inputText
        while (DotlinTools.isNotEmpty(remainingText))
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val currentChar = remainingText.get(0).toString() // workaround for dotlin for: for (c in text)

            /*
            DotlinLogger.log("---")
            DotlinLogger.log("currentChar:     ${Tools.toDisplayStringSimple(currentChar)}")
            DotlinLogger.log("currentBrackets: ${Tools.toDisplayStringForStrings(currentBrackets)}")
            DotlinLogger.log("currentText:     ${Tools.toDisplayString(currentText)}")
            DotlinLogger.log("header:          ${Tools.toDisplayString(header)}")
            DotlinLogger.log("remainingText:   ${Tools.toDisplayString(remainingText)}")
            */

            if (remainingText.startsWith("//") || remainingText.startsWith("/*"))
            {
                val extractionResult = CommentExtractor.extract(remainingText)
                currentText += extractionResult.comment
                remainingText = extractionResult.remainingText
                continue
            }

            if (currentChar == ";" && DotlinTools.isEmpty(currentBrackets))
            {
                currentText += currentChar
                remainingText = DotlinTools.substring(remainingText, 1)

                if (DotlinTools.startsWith(DotlinTools.trim(remainingText), "else"))
                {
                    if (DotlinTools.startsWith(DotlinTools.trim(remainingText), "else "))
                    {
                        //DotlinLogger.log("Expecting 'else' branch.")
                        //DotlinLogger.log("currentText:   ${Tools.toDisplayString(currentText)}")
                        //DotlinLogger.log("remainingText: ${Tools.toDisplayString(remainingText)}")
                        continue
                    }

                    TODO("whitespace after else")
                }

                return SplitResult(remainingText, listOf(Statement(currentText)))
            }

            if (currentChar == "{" && DotlinTools.isEmpty(currentBrackets))
            {
                currentText += currentChar
                remainingText = DotlinTools.substring(remainingText, 1)

                val result = MasterSplitter().split(remainingText)
                remainingText = result.remainingText

                if (!DotlinTools.startsWith(remainingText, "}"))
                {
                    DotlinLogger.log("---")
                    DotlinLogger.log("currentChar:     ${Tools.toDisplayStringSimple(currentChar)}")
                    DotlinLogger.log("currentBrackets: ${Tools.toDisplayStringForStrings(currentBrackets)}")
                    DotlinLogger.log("currentText:     ${Tools.toDisplayString(currentText)}")
                    DotlinLogger.log("header:          ${Tools.toDisplayString(header)}")
                    DotlinLogger.log("remainingText:   ${Tools.toDisplayString(remainingText)}")
                    TODO("error 1 ${Tools.toDisplayString(currentText)} ${Tools.toDisplayString(header)} ${Tools.toDisplayString(remainingText)}")
                }

                if (isDoubleBlock)
                {
                    if (remainingText == "}")
                        return SplitResult("", listOf(DoubleBlock(header, currentText, "}", parts1, result.parts)))

                    DotlinLogger.log("---")
                    DotlinLogger.log("currentChar:     ${Tools.toDisplayStringSimple(currentChar)}")
                    DotlinLogger.log("currentBrackets: ${Tools.toDisplayStringForStrings(currentBrackets)}")
                    DotlinLogger.log("currentText:     ${Tools.toDisplayString(currentText)}")
                    DotlinLogger.log("header:          ${Tools.toDisplayString(header)}")
                    DotlinLogger.log("remainingText:   ${Tools.toDisplayString(remainingText)}")
                    TODO("is double block")
                }

                if (remainingText == "}")
                {
                    //DotlinLogger.log("- Returning SingleBlock (remainingText == \"}\")")
                    return SplitResult("", listOf(SingleBlock(currentText, "}", result.parts)))
                }

                if (DotlinTools.startsWith(DotlinTools.trim(DotlinTools.substring(remainingText, 1)), "else"))
                {
                    if (DotlinTools.startsWith(DotlinTools.trim(DotlinTools.substring(remainingText, 1)), "else "))
                    {
                        //DotlinLogger.log("Expecting 'else' branch.")
                        //DotlinLogger.log("c:               ${Tools.toDisplayString(c)}")
                        //DotlinLogger.log("currentBrackets: ${Tools.toDisplayStringForStrings(currentBrackets)}")
                        //DotlinLogger.log("currentText:     ${Tools.toDisplayString(currentText)}")
                        //DotlinLogger.log("remainingText:   ${Tools.toDisplayString(remainingText)}")
                        //DotlinLogger.log("result.parts:    ${Tools.toDisplayStringForParts(result.parts)}")
                        isDoubleBlock = true
                        header = currentText
                        parts1 = result.parts
                        currentText = "}"
                        remainingText = DotlinTools.substring(remainingText, 1)
                        continue
                    }

                    TODO("whitespace after else")
                }

                /*
                DotlinLogger.log("---")
                DotlinLogger.log("currentChar:     ${Tools.toDisplayStringSimple(currentChar)}")
                DotlinLogger.log("currentBrackets: ${Tools.toDisplayStringForStrings(currentBrackets)}")
                DotlinLogger.log("currentText:     ${Tools.toDisplayString(currentText)}")
                DotlinLogger.log("header:          ${Tools.toDisplayString(header)}")
                DotlinLogger.log("remainingText:   ${Tools.toDisplayString(remainingText)}")
                DotlinLogger.log("- Returning SingleBlock (no else)")
                */
                return SplitResult(remainingText.substring(1), listOf(SingleBlock(currentText, "}", result.parts)))
            }

            if (Tools.isOpeningBracket(currentChar))
                currentBrackets.add(currentChar)

            if (Tools.isClosingBracket(currentChar))
            {
                if (DotlinTools.isEmpty(currentBrackets))
                {
                    DotlinLogger.log("---")
                    DotlinLogger.log("currentChar:     ${Tools.toDisplayStringSimple(currentChar)}")
                    DotlinLogger.log("currentBrackets: ${Tools.toDisplayStringForStrings(currentBrackets)}")
                    DotlinLogger.log("currentText:     ${Tools.toDisplayString(currentText)}")
                    DotlinLogger.log("header:          ${Tools.toDisplayString(header)}")
                    DotlinLogger.log("remainingText:   ${Tools.toDisplayString(remainingText)}")
                    throw DartFormatException("Unexpected closing curly bracket.")
                }

                val lastOpeningBracket = currentBrackets.removeLast()
                val expectedClosingBracket = Tools.getClosingBracket(lastOpeningBracket)
                if (currentChar != expectedClosingBracket)
                    TODO("error 2 ${Tools.toDisplayString(remainingText)}")
            }

            currentText += currentChar
            remainingText = DotlinTools.substring(remainingText, 1)
        }

        DotlinLogger.log("---")
        DotlinLogger.log("currentBrackets: ${Tools.toDisplayStringForStrings(currentBrackets)}")
        DotlinLogger.log("currentText:     ${Tools.toDisplayString(currentText)}")
        DotlinLogger.log("header:          ${Tools.toDisplayString(header)}")
        DotlinLogger.log("remainingText:   ${Tools.toDisplayString(remainingText)}")
        throw DartFormatException("Unexpected end of block or statement.")
    }
}
