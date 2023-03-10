package dev.eggnstone.plugins.jetbrains.dartformat.extractors

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

class CommentExtractor
{
    companion object
    {
        fun extract(inputText: String): ExtractionResult
        {
            if (DotlinTools.isEmpty(inputText))
                return ExtractionResult("", "")

            if (DotlinTools.startsWith(inputText, "//"))
            {
                //DotlinLogger.log("CommentExtractor.extract(${Tools.toDisplayString(inputText)})")

                val nextLinePos = Tools.getNextLinePos(inputText)
                //DotlinLogger.log("nextLinePos: $nextLinePos")

                if (nextLinePos == -1)
                    return ExtractionResult(inputText, "")

                val comment = DotlinTools.substring(inputText, 0, nextLinePos)
                val remainingText = DotlinTools.substring(inputText, nextLinePos)

                return ExtractionResult(comment, remainingText)
            }

            if (DotlinTools.startsWith(inputText, "/*"))
            {
                var comment = "/*"

                @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
                for (i in 2 until inputText.length) // workaround for dotlin
                {
                    if (i + 1 < inputText.length)
                    {
                        val sub = DotlinTools.substring(inputText, i, i + 2)
                        if (sub == "*/")
                            return ExtractionResult(comment + sub, DotlinTools.substring(inputText, i + 2))
                    }

                    @Suppress("ReplaceGetOrSet") // workaround for dotlin
                    val c = inputText.get(i).toString() // workaround for dotlin
                    comment += c
                }

                return ExtractionResult(comment, "")
            }

            throw DartFormatException("Unexpected non-Comment text.")
        }
    }
}
