package dev.eggnstone.plugins.jetbrains.dartformat.levels

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TypeSplitter

class LevelsCalculator
{
    fun calcLevels(line: String, lineIndex: Int, oldBracketPackages: List<BracketPackage>): Levels
    {
        //if (DotlinLogger.isEnabled) DotlinLogger.log("LevelsCalculator.calcLevels(line=${Tools.toDisplayString(line)}, oldBracketPackages=${oldBracketPackages.size})")

        if (StringWrapper.isEmpty(line))
            return Levels(0, listOf())

        var brackets = 0
        var conditionals = 0

        val currentBracketPackages = oldBracketPackages.toMutableList()
        var currentBrackets = mutableListOf<String>()
        //var currentLineIndex = lineIndex

        val items = TypeSplitter().split(line)
        //if (DotlinLogger.isEnabled) DotlinLogger.log("  items: (${Tools.toDisplayStringForStrings(items)})")

        for (item in items)
        {
            if (item == "//")
                break

            if (item == "do" || item == "for" || item == "if" || item == "while")
            {
                conditionals++
                continue
            }

            if (item.length == 1 && Tools.isOpeningBracket(item))
            {
                brackets++
                //currentBrackets += item dotlin
                currentBrackets.add(item)
                continue
            }

            if (item.length == 1 && Tools.isClosingBracket(item))
            {
                if (DotlinTools.isEmpty(currentBrackets))
                {
                    // TODO: what to do when one bracket is removed and therefore an old bracket package is used but then new brackets are added?
                    if (DotlinTools.isEmpty(currentBracketPackages))
                        throw DartFormatException("DotlinTools.isEmpty(currentBrackets)")

                    val tempBracketPackage = currentBracketPackages.removeLast()
                    currentBrackets = tempBracketPackage.brackets.toMutableList()
                    //currentLineIndex = tempBracketPackage.lineIndex
                }

                val lastItem = DotlinTools.last(currentBrackets)
                //if (item != Tools.getClosingBracket(currentBrackets.last())) dotlin
                if (item != Tools.getClosingBracket(lastItem))
                    TODO("LevelsCalculator.calcLevels: item != Tools.getClosingBracket(lastItem)") // throw DartFormatException("item != currentBrackets.last() Expected: $lastItem Is: $item")

                brackets--
                currentBrackets.removeLast()

                continue
            }

            /*if (item.length > 1 && (item.startsWith("{") || item.startsWith("}")))
                TODO()*/

            //if (DotlinLogger.isEnabled) DotlinLogger.log("    ${Tools.toDisplayString(item)} = ? -> nothing")
        }

        if (DotlinTools.isNotEmpty(currentBrackets))
        {
            //currentBracketPackages += BracketPackage(currentBrackets, lineIndex) dotlin
            currentBracketPackages.add(BracketPackage(currentBrackets, lineIndex))
        }

        return Levels(conditionals, currentBracketPackages)
    }
}
