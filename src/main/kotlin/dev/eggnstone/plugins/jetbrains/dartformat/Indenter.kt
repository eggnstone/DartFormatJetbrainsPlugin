package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace

class Indenter
{
    fun indent(parts: List<IPart>): String
    {
        var currentLevel = 0
        var currentLine = ""
        //var isConditional = false
        var conditionals = 0
        val lineSplitter = LineSplitter()
        var nextLevel = 0
        var result = ""

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (partIndex in 0 until parts.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val part = parts.get(partIndex) // workaround for dotlin
            DotlinLogger.log("Part #$partIndex: ${Tools.toDisplayString(part.toString())}")

            if (part is Whitespace)
            {
                if (DotlinTools.isNotEmpty(currentLine))
                    TODO()

                continue
            }

            /*
            if (true) if (true)
                abc();

            if (true) if (true) if (true) if (true)
                        {
                        }
                        else
                        {
                        }
                    else
                    {
                    }
                else
                {
                }
            else
            {
            }
            */

            val recreatedPart = part.recreate()
            val lines = lineSplitter.split(recreatedPart)
            DotlinLogger.log("  Lines ${Tools.toDisplayStringForStrings(lines)}")
            @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
            for (lineIndex in 0 until lines.size) // workaround for dotlin
            {
                @Suppress("ReplaceGetOrSet") // workaround for dotlin
                val line = lines.get(lineIndex) // workaround for dotlin
                val levels = LevelsCalculator.calcLevels(line)

                val currentLevelText = if (levels.currentLevel < 0) "-" + levels.currentLevel.toString() else if (levels.currentLevel == 0) "0" else "+" + levels.currentLevel.toString()
                val nextLevelText = if (levels.nextLevel < 0) "-" + levels.nextLevel.toString() else if (levels.nextLevel == 0) "0" else "+" + levels.nextLevel.toString()
                val conditionalsText = if (levels.conditionals < 0) "-" + levels.conditionals.toString() else if (levels.conditionals == 0) "0" else "+" + levels.conditionals.toString()
                val oldConditionals = conditionals
                val oldCurrentLevel = currentLevel
                val oldNextLevel = nextLevel
                conditionals += levels.conditionals
                currentLevel += levels.currentLevel
                nextLevel += levels.nextLevel
                DotlinLogger.log("    Line #$lineIndex: ${Tools.toDisplayString(line)}    curr=$currentLevelText    next=$nextLevelText    cond=$conditionalsText")
                DotlinLogger.log("      conditionals:  $oldConditionals + ${levels.conditionals} = $conditionals")
                DotlinLogger.log("      currentLevel:  $oldCurrentLevel + ${levels.currentLevel} = $currentLevel")
                DotlinLogger.log("      nextLevel:     $oldNextLevel + ${levels.nextLevel} = $nextLevel")

                if (oldConditionals > 0 && levels.currentLevel > 0 && levels.nextLevel > 0)
                {
                    DotlinLogger.log("      --")
                    conditionals--
                    currentLevel--
                    nextLevel--
                }
                /*else
                    DotlinLogger.log("      no--")*/

                /*if (line.startsWith("if"))
                {
                    //isConditional = true
                }
                else if (line.startsWith("{"))
                {
                    //cond
                    *//*if (isConditional)
                    {
                        currentLevel--
                        nextLevel--
                    }
                    else
                    {
                        //OK
                    }*//*
                }
                else if (line.startsWith("}"))
                {
                    if (currentLevel > 0 && nextLevel > 0)
                    {
                        TODO()
                    }
                    else
                    {
                        //TODO()
                    }
                }*/

                val pad = DotlinTools.getSpaces(currentLevel * 4)

                result += pad + line

                currentLevel = nextLevel
            }
        }

        return result
    }
}
