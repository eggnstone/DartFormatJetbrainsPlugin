package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.levels.LevelsCalculator
import dev.eggnstone.plugins.jetbrains.dartformat.parts.*

class MasterIndenter : IIndenter
{
    companion object
    {
        private val levelsCalculator = LevelsCalculator()
    }

    override fun indentPart(part: IPart): String
    {
        //DotlinLogger.log("MasterIndenter.indentPart: $part")

        val indenter = getIndenter(part)
        return indenter.indentPart(part)
    }

    /*fun indentPart2(part: IPart): String
    {
        var currentLevel = 0
        var currentLine = ""
        //var isConditional = false
        var conditionals = 0
        val lineSplitter = LineSplitter()
        var nextLevel = 0
        var result = ""

        DotlinLogger.log("Part: ${Tools.toDisplayString(part.toString())}")

        if (part is Whitespace)
        {
            if (DotlinTools.isNotEmpty(currentLine))
                TODO()

            TODO()
        }

        *//*
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
        *//*

        val recreatedPart = part.recreate()
        val lines = lineSplitter.split(recreatedPart)
        DotlinLogger.log("  Lines ${Tools.toDisplayStringForStrings(lines)}")
        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until lines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val line = lines.get(i) // workaround for dotlin
            val levels = levelsCalculator.calcLevels(line)

            val currentLevelText = if (levels.currentLevel < 0) "-" + levels.currentLevel.toString() else if (levels.currentLevel == 0) "0" else "+" + levels.currentLevel.toString()
            val nextLevelText = if (levels.nextLevel < 0) "-" + levels.nextLevel.toString() else if (levels.nextLevel == 0) "0" else "+" + levels.nextLevel.toString()
            val conditionalsText = if (levels.conditionals < 0) "-" + levels.conditionals.toString() else if (levels.conditionals == 0) "0" else "+" + levels.conditionals.toString()
            val oldConditionals = conditionals
            val oldCurrentLevel = currentLevel
            val oldNextLevel = nextLevel
            conditionals += levels.conditionals
            currentLevel += levels.currentLevel
            nextLevel += levels.nextLevel
            DotlinLogger.log("    Line #$i: ${Tools.toDisplayString(line)}    curr=$currentLevelText    next=$nextLevelText    cond=$conditionalsText")
            DotlinLogger.log("      conditionals:  $oldConditionals + ${levels.conditionals} = $conditionals")
            DotlinLogger.log("      currentLevel:  $oldCurrentLevel + ${levels.currentLevel} = $currentLevel")
            DotlinLogger.log("      nextLevel:     $oldNextLevel + ${levels.nextLevel} = $nextLevel")

            if (oldConditionals > 0 && levels.currentLevel > 0 && levels.nextLevel > 0)
            {
                DotlinLogger.log("      levels--")
                conditionals--
                currentLevel--
                nextLevel--
            }
            *//*else
                DotlinLogger.log("      no levels--")*//*

            *//*if (line.startsWith("if"))
            {
                //isConditional = true
            }
            else if (line.startsWith("{"))
            {
                //cond
                *//**//*if (isConditional)
                    {
                        currentLevel--
                        nextLevel--
                    }
                    else
                    {
                        //OK
                    }*//**//*
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
                }*//*

            val pad = DotlinTools.getSpaces(currentLevel * 4)

            result += pad + line

            currentLevel = nextLevel
        }

        return result
    }*/

    fun indentParts(parts: List<IPart>): String
    {
        if (DotlinTools.isEmpty(parts))
            return ""

        var result = ""

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until parts.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val part = parts.get(i) // workaround for dotlin
            result += indentPart(part)
        }

        return result
    }

    fun getIndenter(inputPart: IPart): IIndenter
    {
        @Suppress("LiftReturnOrAssignment")
        when (inputPart)
        {
            is DoubleBlock -> return DoubleBlockIndenter()
            is SingleBlock -> return SingleBlockIndenter()
            is Statement -> return StatementIndenter()
            is Whitespace -> return WhitespaceIndenter()
            else -> TODO()
        }
    }
}
