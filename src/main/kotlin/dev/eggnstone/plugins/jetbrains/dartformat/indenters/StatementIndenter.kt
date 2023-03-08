package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.LineSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement

class StatementIndenter : IIndenter
{
    companion object
    {
        private val levelsCalculator = LevelsCalculator()
    }

    override fun indentPart(part: IPart): String
    {
        //DotlinLogger.log("StatementIndenter.indentPart: ${Tools.toDisplayString(part.toString())}")

        if (part !is Statement)
            throw DartFormatException("Unexpected non-Statement type.")

        val recreatedPart = part.recreate()
        val lines = LineSplitter().split(recreatedPart)

        var currentLevel = 0
        var result = ""

        @Suppress("ReplaceManualRangeWithIndicesCalls")
        for (i in 0 until lines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val line = lines.get(i) // workaround for dotlin

            val levels = levelsCalculator.calcLevels(line)
            //DotlinLogger.log("  levels: ($levels)")
            //DotlinLogger.log("    currentLevel: $currentLevel")

            val tempLevel = currentLevel + levels.currentLevel
            val pad = DotlinTools.getSpaces(tempLevel * 4)
            result += pad + line
            currentLevel += levels.nextLevel
            //DotlinLogger.log("    currentLevel: $currentLevel")

            /*if (lineIndex == 0)
            {
                result += line
            }
            else
            {
                val pad = DotlinTools.getSpaces( currentLevel*4)

                result += pad + line
            }*/
        }

        return result
    }
}
