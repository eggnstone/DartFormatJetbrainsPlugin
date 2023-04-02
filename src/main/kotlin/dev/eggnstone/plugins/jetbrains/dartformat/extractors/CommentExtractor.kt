package dev.eggnstone.plugins.jetbrains.dartformat.extractors

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

class CommentExtractor
{
    companion object
    {
        fun extract(inputText: String, currentIndent: Int): ExtractionResult
        {
            if (DotlinLogger.isEnabled) DotlinLogger.log("CommentExtractor.extract(currentIndent=$currentIndent, ${Tools.toDisplayStringShort(inputText)})")

            if (StringWrapper.isEmpty(inputText))
                return ExtractionResult("", currentIndent, "")

            if (StringWrapper.startsWith(inputText, "//"))
            {
                val nextLinePos = Tools.getNextLinePos(inputText)
                //if (DotlinLogger.isEnabled) DotlinLogger.log("nextLinePos: $nextLinePos")

                if (nextLinePos == -1)
                    return ExtractionResult(inputText, currentIndent, "")

                val comment = StringWrapper.substring(inputText, 0, nextLinePos)
                val remainingText = StringWrapper.substring(inputText, nextLinePos)

                return ExtractionResult(comment, currentIndent, remainingText)
            }

            if (StringWrapper.startsWith(inputText, "/*"))
            {
                var comment = "/*"

                for (i in 2 until inputText.length) // workaround for dotlin
                {
                    if (i + 1 < inputText.length)
                    {
                        val sub = StringWrapper.substring(inputText, i, i + 2)
                        if (sub == "*/")
                            return ExtractionResult(comment + sub, currentIndent, StringWrapper.substring(inputText, i + 2))
                    }

                    @Suppress("ReplaceGetOrSet") // workaround for dotlin
                    val c = inputText.get(i).toString() // workaround for dotlin
                    comment += c
                }

                TODO("CommentExtractor.extract") // return ExtractionResult(comment, "")
            }

            throw DartFormatException("Unexpected non-Comment text.")
        }
    }
}
