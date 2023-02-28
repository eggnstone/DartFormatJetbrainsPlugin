package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

class LevelsCalculator
{
    fun calcLevels(line: String): Levels
    {
        if (DotlinTools.isEmpty(line))
            return Levels(0, 0, 0)

        var conditionals = 0
        var currentLevel = 0
        var nextLevel = 0
        val items = WhitespaceSplitter().split(line)
        for (item in items)
        {
            if (item == "if")
            {
                conditionals++
                nextLevel++
                continue
            }

            if (item.length == 1 && Tools.isOpeningBracket(item))
            {
                nextLevel++
                continue
            }

            if (item.length == 1 && Tools.isClosingBracket(item))
            {
                currentLevel--
                nextLevel--
                continue
            }
        }

        return Levels(currentLevel, nextLevel, conditionals)
    }
}
