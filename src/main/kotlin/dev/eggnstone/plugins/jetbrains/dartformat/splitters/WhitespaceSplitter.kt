package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace

class WhitespaceSplitter : ISplitter
{
    override fun split(inputText: String): SplitResult
    {
        println("WhitespaceSplitter.split: ${Tools.shorten(inputText, 100)}")

        if (inputText.isEmpty())
            throw DartFormatException("Unexpected empty text.")

        var whitespace = ""

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

            val remainingText = inputText.substring(i)
            return SplitResult(remainingText, listOf(Whitespace(whitespace)))
        }

        return SplitResult("", listOf(Whitespace(inputText)))
    }
}
