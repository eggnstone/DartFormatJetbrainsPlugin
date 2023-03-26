package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.LineSplitter

class CommentIndenter(private val spacesPerLevel: Int) : IIndenter
{
    companion object
    {
        private val lineSplitter = LineSplitter()
    }

    override fun indentPart(part: IPart, startIndent: Int, indentLevel: Int): String
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("CommentIndenter.indentPart(startIndent=$startIndent, indentLevel=$indentLevel, part=$part)")

        if (part !is Comment)
            throw DartFormatException("Unexpected non-Comment type.")

        val comment: Comment = part

        val recreatedPart = comment.recreate()

        if (comment.startPos == 0 && indentLevel == 0)
            return recreatedPart

        val diff = startIndent - comment.startPos
        if (DotlinLogger.isEnabled) DotlinLogger.log("  diff=$diff")

        var result = ""
        val lines = lineSplitter.split(recreatedPart, trim = false)
        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until lines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val line = lines.get(i) // workaround for dotlin
            if (DotlinLogger.isEnabled) DotlinLogger.log("Line #$i: leadingSpaces=${Tools.countLeadingSpaces(line)} ${Tools.toDisplayStringShort(line)}")

            var spaces = indentLevel * spacesPerLevel
            if (i > 0)
                spaces += diff

            result += if (spaces >= 0)
            {
                StringWrapper.getSpaces(spaces) + line
            }
            else
            {
                val leadingSpaces = Tools.countLeadingSpaces(line)
                spaces += leadingSpaces
                if (spaces < 0)
                {
                    if (DotlinLogger.isEnabled) DotlinLogger.log("WARNING: spaces < 0")
                    //TODO("spaces < 0")
                    spaces = 0
                }

                StringWrapper.getSpaces(spaces) + Tools.trimStartSimple(line)
            }
        }

        return result
    }
}
