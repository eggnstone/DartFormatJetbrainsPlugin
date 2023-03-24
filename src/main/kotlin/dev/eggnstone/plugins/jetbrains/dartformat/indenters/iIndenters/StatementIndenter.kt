package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.levels.BracketPackage
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.LineSplitter

class StatementIndenter(private val spacesPerLevel: Int) : IIndenter
{
    companion object
    {
        private val levelsCalculator = LevelsCalculator()
    }

    override fun indentPart(part: IPart, currentLevel: Int): String
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("StatementIndenter.indentPart: $part")

        if (part !is Statement)
            throw DartFormatException("Unexpected non-Statement type.")

        val recreatedPart = part.recreate()
        val lines = LineSplitter().split(recreatedPart, true)

        var currentConditionals = 0
        var usesColon = false
        var result = ""

        var currentBracketPackages: List<BracketPackage> = listOf() // ok
        //var currentBracketPackages = listOf<BracketPackage>()

        @Suppress("ReplaceManualRangeWithIndicesCalls")
        for (lineIndex in 0 until lines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val line = lines.get(lineIndex) // workaround for dotlin
            if (DotlinLogger.isEnabled) DotlinLogger.log("  Line #$lineIndex: ${Tools.toDisplayString(line)}")

            var startsWithColon = false
            if (!usesColon)
            {
                startsWithColon = StringWrapper.startsWith(line, ":")
                if (startsWithColon)
                    usesColon = true
            }

            val levels = levelsCalculator.calcLevels(line, lineIndex, currentBracketPackages)
            //if (DotlinLogger.isEnabled) DotlinLogger.log("    currentConditionals: $currentConditionals")
            //if (DotlinLogger.isEnabled) DotlinLogger.log("    newConditionals:     ${levels.newConditionals}")
            //if (DotlinLogger.isEnabled) DotlinLogger.log("    newBracketPackages:  ${levels.newBracketPackages}")

            //val tempLevel = currentLevel + levels.currentLevel
            val tempLevel = currentConditionals + DotlinTools.minOf(currentBracketPackages.size, levels.newBracketPackages.size)
            //val tempLevel = currentConditionals + levels.newBracketPackages.size
            var pad = StringWrapper.getSpaces(tempLevel * spacesPerLevel)

            if (usesColon)
            {
                pad = StringWrapper.getSpaces(spacesPerLevel) + pad
                if (!startsWithColon)
                    pad = "  $pad"
            }

            result += pad + line
            //currentLevel += levels.nextLevel
            currentConditionals += levels.newConditionals
            //currentLevel = currentConditionals + levels.newBracketPackages.size
            //if (DotlinLogger.isEnabled) DotlinLogger.log("    currentLevel: $currentLevel")
            currentBracketPackages = levels.newBracketPackages
        }

        return result
    }
}
