package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class MasterSplitter : ISplitter
{
    fun splitAll(inputText: String): List<IPart>
    {
        val splitResult = split(inputText)
        if (DotlinTools.isNotEmpty(splitResult.remainingText))
        {
            DotlinLogger.log("MasterSplitter.splitAll")
            DotlinLogger.log("  parts:         ${Tools.toDisplayStringForParts(splitResult.parts)}")
            DotlinLogger.log("  remainingText: ${Tools.toDisplayString(splitResult.remainingText)}")
            throw DartFormatException("splitResult.remainingText.isNotEmpty()")
        }

        return splitResult.parts
    }

    override fun split(inputText: String): SplitResult
    {
        DotlinLogger.log("MasterSplitter.split: ${Tools.shorten(inputText, 100, true)}")

        val parts = mutableListOf<IPart>()

        var remainingText = inputText
        while (DotlinTools.isNotEmpty(remainingText))
        {
            val splitter = getSplitter(remainingText)
            @Suppress("FoldInitializerAndIfToElvis")
            if (splitter == null)
                return SplitResult(remainingText, parts)

            val splitResult = splitter.split(remainingText)
            ///*
            DotlinLogger.log("Result from splitter:")
            DotlinLogger.log("  parts:         ${Tools.toDisplayStringForParts(splitResult.parts)}")
            DotlinLogger.log("  remainingText: ${Tools.toDisplayString(splitResult.remainingText)}")
            //*/
            remainingText = splitResult.remainingText
            //parts += result.parts // dotlin
            parts.addAll(splitResult.parts)
        }

        return SplitResult("", parts)
    }

    fun getSplitter(inputText: String): ISplitter?
    {
        if (DotlinTools.isEmpty(inputText))
            throw DartFormatException("Unexpected empty text.")

        @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
        val c = inputText.get(0).toString() // workaround for dotlin for: for (c in text)

        if (Tools.isWhitespace(c))
            return WhitespaceSplitter()

        if (c != "}")
            return TextSplitter()

        return null
    }
}