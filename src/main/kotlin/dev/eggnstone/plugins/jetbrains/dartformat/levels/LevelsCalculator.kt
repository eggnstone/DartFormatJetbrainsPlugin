package dev.eggnstone.plugins.jetbrains.dartformat.levels

import dev.eggnstone.plugins.jetbrains.dartformat.TypeSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

class LevelsCalculator
{
    fun calcLevels(line: String): Levels
    {
        if (DotlinTools.isEmpty(line))
            return Levels(0, 0, 0)

        var brackets = 0
        var conditionals = 0
        var currentLevel = 0
        var nextLevel = 0
        val items = TypeSplitter().split(line)
        //DotlinLogger.log("  items: (${Tools.toDisplayStringForStrings(items)})")

        for (item in items)
        {
            if (item == "if")
            {
                //DotlinLogger.log("    if -> conditionals++ / nextLevel++")
                conditionals++
                nextLevel++
                continue
            }

            if (item.length == 1 && item == "{")// Tools.isOpeningBracket(item))
            {
                //DotlinLogger.log("    ${Tools.toDisplayString(item)}  = opening -> nextLevel++")
                //DotlinLogger.log("    ${Tools.toDisplayString(item)}  = opening { -> nextLevel++")
                brackets++
                //nextLevel++
                continue
            }

            if (item.length == 1 && item == "}")// Tools.isClosingBracket(item))
            {
                //DotlinLogger.log("    ${Tools.toDisplayString(item)} = closing -> currentLevel-- / nextLevel--")
                //DotlinLogger.log("    ${Tools.toDisplayString(item)} = closing } -> currentLevel-- / nextLevel--")
                brackets--
                //currentLevel--
                //nextLevel--
                continue
            }

            /*if (item.length > 1 && (item.startsWith("{") || item.startsWith("}")))
                TODO()*/

            //DotlinLogger.log("    ${Tools.toDisplayString(item)} = ? -> nothing")
        }

        if (brackets < 0)
            currentLevel += brackets

        nextLevel += brackets

        return Levels(currentLevel, nextLevel, conditionals)
    }
}
