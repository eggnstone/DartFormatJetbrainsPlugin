package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
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

    override fun indentPart(part: IPart, currentLevel: Int): String
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("CommentIndenter.indentPart: $part")

        if (part !is Comment)
            throw DartFormatException("Unexpected non-Comment type.")

        val recreatedPart = part.recreate()

        if (currentLevel == 0)
            return recreatedPart

        var result = ""
        val lines = lineSplitter.split(recreatedPart, trim = false)
        for (line in lines)
        {
            DotlinLogger.log("Line: ${Tools.toDisplayString(line)}")
            result += StringWrapper.getSpaces(currentLevel * spacesPerLevel) + line
        }

        return result
    }
}
