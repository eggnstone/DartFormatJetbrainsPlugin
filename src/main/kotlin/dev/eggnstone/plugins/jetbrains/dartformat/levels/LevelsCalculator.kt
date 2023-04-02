package dev.eggnstone.plugins.jetbrains.dartformat.levels

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.TypeSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

class LevelsCalculator
{
    fun calcLevels(line: String, lineIndex: Int, oldBracketPackages: List<BracketPackage>): Levels
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("LevelsCalculator.calcLevels(line=${Tools.toDisplayStringShort(line)}, oldBracketPackages=${Tools.toDisplayStringForBracketPackages(oldBracketPackages)})")

        if (StringWrapper.isEmpty(line))
            return Levels(0, 0, listOf(), isElse = false, isInSquareBracketIf = false)

        var brackets = 0
        var closedConditionals = 0
        var conditionals = 0
        val currentBracketPackages = oldBracketPackages.toMutableList()
        var currentBrackets = mutableListOf<String>()
        var isElse = false
        var isInSquareBracketIf = false
        var isInSingleQuoteString = false
        var isInDoubleQuoteString = false
        var isInMultiLineComment = false
        val isInSquareBrackets = DotlinTools.isNotEmpty(oldBracketPackages) && DotlinTools.isNotEmpty(oldBracketPackages.last().brackets) && oldBracketPackages.last().brackets.last() == "["

        val items = TypeSplitter().split(line)
        if (DotlinLogger.isEnabled) DotlinLogger.log("  items: ${Tools.toDisplayStringForStrings(items)}")

        for (item in items)
        {
            if (DotlinLogger.isEnabled)
            {
                DotlinLogger.log("    item: ${Tools.toDisplayStringShort(item)}")
                DotlinLogger.log("    isInSingleQuoteString: $isInSingleQuoteString")
                DotlinLogger.log("    isInDoubleQuoteString: $isInDoubleQuoteString")
                DotlinLogger.log("    isInMultiLineComment:  $isInMultiLineComment")
            }

            if (isInSingleQuoteString)
            {
                if (item == "'")
                    isInSingleQuoteString = false

                continue
            }

            if (isInDoubleQuoteString)
            {
                if (item == "\"")
                    isInDoubleQuoteString = false

                continue
            }

            if (isInMultiLineComment)
            {
                if (item == "*/")
                    isInMultiLineComment = false

                continue
            }

            if (item == "//")
                break

            if (item == "/*")
            {
                isInMultiLineComment = true
                continue
            }

            if (item == "'")
            {
                isInSingleQuoteString = true
                continue
            }

            if (item == "\"")
            {
                isInDoubleQuoteString = true
                continue
            }

            if (item == ",")
            {
                if (isInSquareBracketIf)
                {
                    conditionals--
                    isInSquareBracketIf = false
                }
            }

            if (item == "do" || item == "for" || item == "while")
            {
                conditionals++
                continue
            }

            if (item == "if")
            {
                if (isInSquareBrackets)
                    isInSquareBracketIf = true

                if (!isElse)
                    conditionals++
                continue
            }

            if (item == "else")
            {
                isElse = true
                closedConditionals++
                conditionals++
                continue
            }

            if (item == "else if")
            {
                TODO()
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
                        throw DartFormatException("LevelsCalculator.calcLevels: DotlinTools.isEmpty(currentBrackets)")

                    val tempBracketPackage = currentBracketPackages.removeLast()
                    currentBrackets = tempBracketPackage.brackets.toMutableList()
                    //currentLineIndex = tempBracketPackage.lineIndex
                }

                val lastItem = DotlinTools.last(currentBrackets)
                //if (item != Tools.getClosingBracket(currentBrackets.last())) dotlin
                if (item != Tools.getClosingBracket(lastItem))
                    TODO("LevelsCalculator.calcLevels: item (${Tools.toDisplayStringShort(item)}) != Tools.getClosingBracket(lastItem)") // throw DartFormatException("item != currentBrackets.last() Expected: $lastItem Is: $item")

                brackets--
                currentBrackets.removeLast()

                continue
            }

            /*if (item.length > 1 && (item.startsWith("{") || item.startsWith("}")))
                TODO()*/

            //if (DotlinLogger.isEnabled) DotlinLogger.log("    ${Tools.toDisplayStringShort(item)} = ? -> nothing")
        }

        if (DotlinTools.isNotEmpty(currentBrackets))
        {
            //currentBracketPackages += BracketPackage(currentBrackets, lineIndex) dotlin
            currentBracketPackages.add(BracketPackage(currentBrackets, lineIndex))
        }

        return Levels(conditionals, closedConditionals, currentBracketPackages, isElse, isInSquareBracketIf)
    }
}
