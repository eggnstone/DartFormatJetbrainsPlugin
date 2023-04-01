package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.levels.BracketPackage
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.LineSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

class StatementIndenter(private val spacesPerLevel: Int) : IIndenter
{
    companion object
    {
        private val levelsCalculator = LevelsCalculator()
    }

    override fun indentPart(part: IPart, startIndent: Int, indentLevel: Int): String
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("StatementIndenter.indentPart: $part")

        if (part !is Statement)
            throw DartFormatException("Unexpected non-Statement type.")

        val statement: Statement = part

        val recreatedPart = statement.recreate()
        val lines = LineSplitter().split(recreatedPart, true)

        var currentBracketPackages: List<BracketPackage> = listOf() // ok
        //var currentBracketPackages = listOf<BracketPackage>()
        var currentConditionals = 0
        var isSwitch = false
        var usesColon = false

        var result = ""

        @Suppress("ReplaceManualRangeWithIndicesCalls")
        for (lineIndex in 0 until lines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val line = lines.get(lineIndex) // workaround for dotlin
            if (DotlinLogger.isEnabled) DotlinLogger.log("  Line #$lineIndex: ${Tools.toDisplayStringShort(line)}")

            var startsWithColon = false
            if (!usesColon)
            {
                startsWithColon = StringWrapper.startsWith(line, ":")
                if (startsWithColon)
                    usesColon = true
            }

            if (lineIndex == 0)
            {
                var caseOrDefaultEndPos = Tools.getTextEndPos(line, "case")
                if (caseOrDefaultEndPos < 0)
                    caseOrDefaultEndPos = Tools.getTextEndPos(line, "default")

                if (caseOrDefaultEndPos >= 0)
                    isSwitch = true
            }

            val switchLevel = if (isSwitch) if (lineIndex == 0) 0 else 1 else 0
            val levels = levelsCalculator.calcLevels(line, lineIndex, currentBracketPackages)
            if (DotlinLogger.isEnabled)
            {
                DotlinLogger.log("    currentConditionals:   $currentConditionals")
                DotlinLogger.log("    newConditionals:       ${levels.newConditionals}")
                DotlinLogger.log("    newClosedConditionals: ${levels.newClosedConditionals}")
                DotlinLogger.log("    newBracketPackages:    ${levels.newBracketPackages}")
                DotlinLogger.log("    isElse:                ${levels.isElse}")
            }

            /*if (levels.isElse && currentConditionals == 0)
                throw DartFormatException("levels.isElse && currentConditionals == 0")*/

            //val adjustedCurrentConditionals = currentConditionals + (if (levels.isElse) -1 else 0)
            val adjustedCurrentConditionals = currentConditionals + (if (levels.newClosedConditionals > 0) -1 else 0)
            val tempLevel = adjustedCurrentConditionals + DotlinTools.minOf(currentBracketPackages.size, levels.newBracketPackages.size)
            var pad = StringWrapper.getSpaces((switchLevel + tempLevel) * spacesPerLevel)

            if (usesColon)
            {
                pad = StringWrapper.getSpaces(spacesPerLevel) + pad
                if (!startsWithColon)
                    pad = "  $pad"
            }

            result += pad + line
            //currentLevel += levels.nextLevel
            currentConditionals += levels.newConditionals - levels.newClosedConditionals
            //currentLevel = currentConditionals + levels.newBracketPackages.size
            //if (DotlinLogger.isEnabled) DotlinLogger.log("    currentLevel: $currentLevel")
            currentBracketPackages = levels.newBracketPackages
        }

        return result
    }
}
