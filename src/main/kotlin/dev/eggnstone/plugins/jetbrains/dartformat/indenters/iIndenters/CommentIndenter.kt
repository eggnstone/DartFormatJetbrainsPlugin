package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Comment
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class CommentIndenter(private val spacesPerLevel: Int) : IIndenter
{
    companion object
    {
        private val levelsCalculator = LevelsCalculator()
    }

    override fun indentPart(part: IPart): String
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("CommentIndenter.indentPart: $part")

        if (part !is Comment)
            throw DartFormatException("Unexpected non-Comment type.")

        val recreatedPart = part.recreate()
        //val lines = LineSplitter().split(recreatedPart, true)

        var result = ""

        result += recreatedPart

        return result
    }
}
