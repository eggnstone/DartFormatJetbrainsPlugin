package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace

class WhitespaceSplitter : ISplitter
{
    override val name = "Whitespace"

    override fun split(inputText: String, params: SplitParams, inputCurrentIndent: Int): SplitResult
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("WhitespaceSplitter.split: ${Tools.toDisplayStringShort(inputText)}")

        if (StringWrapper.isEmpty(inputText))
            throw DartFormatException("Unexpected empty text.")

        var whitespace = ""

        /*val nextLinePos = Tools.getNextLinePos(inputText)
        if (DotlinLogger.isEnabled) DotlinLogger.log("nextLinePos: $nextLinePos")

        if (nextLinePos == -1)
            TODO()

        TODO()*/

        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until inputText.length) // workaround for dotlin for: for (c in text)
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
            val c = inputText.get(i).toString() // workaround for dotlin for: for (c in text)

            if (Tools.isWhitespace(c))
            {
                whitespace += c
                continue
            }

            if (i == 0)
                throw DartFormatException("Unexpected non-whitespace at beginning of text.")

            val remainingText = StringWrapper.substring(inputText, i)
            return SplitResult(remainingText, listOf(Whitespace(whitespace)))
        }

        return SplitResult("", listOf(Whitespace(inputText)))
    }
}
