package dev.eggnstone.plugins.jetbrains.dartformat.splitters

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

class TypeSplitter
{
    companion object
    {
        val types = listOf(
            //SplitType("Bracket", Tools.Companion::isBracket, false), dotlin
            //SplitType("Whitespace", Tools.Companion::isWhitespace, true), dotlin
            TypeSplitterType("Bracket", false, { currentChar: String, _: String? -> Tools.isBracket(currentChar) }, false),
            TypeSplitterType("Comma", false, { currentChar: String, _: String? -> currentChar == "," }, false),
            TypeSplitterType("EndOfLineComment", true, { currentChar: String, nextChar: String? -> currentChar == "/" && nextChar == "/" }, false),
            TypeSplitterType("MultiLineCommentStart", true, { currentChar: String, nextChar: String? -> currentChar == "/" && nextChar == "*" }, false),
            TypeSplitterType("MultiLineCommentEnd", true, { currentChar: String, nextChar: String? -> currentChar == "*" && nextChar == "/" }, false),
            TypeSplitterType("Whitespace", false, { currentChar: String, _: String? -> Tools.isWhitespace(currentChar) }, true),
        )
    }

    fun split(s: String): List<String>
    {
        val items = mutableListOf<String>()

        var currentText = ""
        var currentType: TypeSplitterType? = null
        var ignoreCurrentChar = false

        @Suppress("ReplaceManualRangeWithIndicesCalls")
        for (i in 0 until s.length)
        {
            if (ignoreCurrentChar)
            {
                ignoreCurrentChar = false
                continue
            }

            @Suppress("ReplaceGetOrSet")
            val currentChar = s.get(i).toString()

            @Suppress("ReplaceGetOrSet")
            val nextChar = if (i < s.length - 1) s.get(i + 1).toString() else null

            /*if (DotlinLogger.isEnabled)
            {
                DotlinLogger.log("Char #$i")
                DotlinLogger.log("  items:        ${Tools.toDisplayStringForStrings(items)}")
                DotlinLogger.log("  currentText:  ${Tools.toDisplayString(currentText)}")
                DotlinLogger.log("  currentChar:  ${Tools.toDisplayString(currentChar)}")
                DotlinLogger.log("  nextChar:     ${Tools.toDisplayString(nextChar ?: "<null>")}")
            }*/

            if (currentType != null)
            {
                if (currentType.function(currentChar, nextChar))
                {
                    if (currentType.combineSimilar)
                    {
                        currentText += currentChar
                        continue
                    }

                    if (StringWrapper.isNotEmpty(currentText))
                        items.add(currentText)

                    currentText = currentChar
                    continue
                }

                currentType = null

                if (StringWrapper.isNotEmpty(currentText))
                    items.add(currentText)

                currentText = ""
            }

            for (type in types)
            {
                if (type.function(currentChar, nextChar))
                {
                    currentType = type

                    if (currentType.useNextChar)
                    {
                        if (StringWrapper.isNotEmpty(currentText))
                            items.add(currentText)

                        currentText = currentChar + nextChar
                        items.add(currentText)
                        currentType = null
                        ignoreCurrentChar = true
                    }
                    else
                    {
                        if (StringWrapper.isNotEmpty(currentText))
                            items.add(currentText)
                    }

                    currentText = ""
                    break
                }
            }

            if (!ignoreCurrentChar)
                currentText += currentChar
        }

        if (StringWrapper.isNotEmpty(currentText))
            items.add(currentText)

        //if (DotlinLogger.isEnabled) DotlinLogger.log("TypeSplitter(${Tools.toDisplayStringShort(s)}) -> ${Tools.toDisplayStringForStrings(items)}")
        return items
    }
}
