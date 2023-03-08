package dev.eggnstone.plugins.jetbrains.dartformat.extractors

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

data class ExtractionResult(val comment: String, val remainingText: String)

class CommentExtractor
{
    companion object
    {
        fun extract(s: String): ExtractionResult
        {
            if (DotlinTools.isEmpty(s))
                return ExtractionResult("", "")

            if (s.startsWith("//"))
            {
                var comment = "//"

                @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
                for (i in 2 until s.length) // workaround for dotlin
                {
                    @Suppress("ReplaceGetOrSet") // workaround for dotlin
                    val c = s.get(i).toString() // workaround for dotlin
                    if (c == "\n" || c == "\r")
                        return ExtractionResult(comment, s.substring(i))

                    comment += c
                }

                return ExtractionResult(comment, "")
            }

            if (s.startsWith("/*"))
            {
                var comment = "/*"

                @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
                for (i in 2 until s.length) // workaround for dotlin
                {
                    if (i + 1 < s.length)
                    {
                        val sub = DotlinTools.substring(s, i, i + 2)
                        if (sub == "*/")
                            return ExtractionResult(comment + sub, s.substring(i + 2))
                    }

                    @Suppress("ReplaceGetOrSet") // workaround for dotlin
                    val c = s.get(i).toString() // workaround for dotlin
                    comment += c
                }

                return ExtractionResult(comment, "")
            }

            throw DartFormatException("Unexpected non-Comment text.")
        }
    }
}
