package dev.eggnstone.plugins.jetbrains.dartformat.extractors

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper

class CommentExtractor
{
    companion object
    {
        fun extract(inputText: String): ExtractionResult
        {
            if (StringWrapper.isEmpty(inputText))
                return ExtractionResult("", "")

            if (StringWrapper.startsWith(inputText, "//"))
            {
                //if (DotlinLogger.isEnabled) DotlinLogger.log("CommentExtractor.extract(${Tools.toDisplayString(inputText)})")

                val nextLinePos = Tools.getNextLinePos(inputText)
                //if (DotlinLogger.isEnabled) DotlinLogger.log("nextLinePos: $nextLinePos")

                if (nextLinePos == -1)
                    return ExtractionResult(inputText, "")

                val comment = StringWrapper.substring(inputText, 0, nextLinePos)
                val remainingText = StringWrapper.substring(inputText, nextLinePos)

                return ExtractionResult(comment, remainingText)
            }

            if (StringWrapper.startsWith(inputText, "/*"))
            {
                var comment = "/*"

                @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
                for (i in 2 until inputText.length) // workaround for dotlin
                {
                    if (i + 1 < inputText.length)
                    {
                        val sub = StringWrapper.substring(inputText, i, i + 2)
                        if (sub == "*/")
                            return ExtractionResult(comment + sub, StringWrapper.substring(inputText, i + 2))
                    }

                    @Suppress("ReplaceGetOrSet") // workaround for dotlin
                    val c = inputText.get(i).toString() // workaround for dotlin
                    comment += c
                }

                TODO("untested") // return ExtractionResult(comment, "")
            }

            throw DartFormatException("Unexpected non-Comment text.")
        }
    }
}
