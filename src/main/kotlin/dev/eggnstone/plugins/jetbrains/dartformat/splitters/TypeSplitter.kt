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
            TypeSplitterType("Bracket", { c: String -> Tools.isBracket(c) }, false),
            TypeSplitterType("Whitespace", { c: String -> Tools.isWhitespace(c) }, true),
        )
    }

    fun split(s: String): List<String>
    {
        val items = mutableListOf<String>()

        var currentText = ""
        var currentType: TypeSplitterType? = null

        @Suppress("ReplaceManualRangeWithIndicesCalls")
        for (i in 0 until s.length)
        {
            @Suppress("ReplaceGetOrSet")
            val c = s.get(i).toString()

            if (currentType != null)
            {
                if (currentType.function(c))
                {
                    if (currentType.combineSimilar)
                    {
                        currentText += c
                        continue
                    }

                    if (StringWrapper.isNotEmpty(currentText))
                        items.add(currentText)

                    currentText = c
                    continue
                }

                currentType = null

                if (StringWrapper.isNotEmpty(currentText))
                    items.add(currentText)

                currentText = ""
            }

            for (type in types)
            {
                if (type.function(c))
                {
                    currentType = type

                    if (StringWrapper.isNotEmpty(currentText))
                        items.add(currentText)

                    currentText = ""

                    break
                }
            }

            currentText += c
        }

        if (StringWrapper.isNotEmpty(currentText))
            items.add(currentText)

        //if (DotlinLogger.isEnabled) DotlinLogger.log("TypeSplitter(${Tools.toDisplayStringShort(s)}) -> ${Tools.toDisplayStringForStrings(items)}")
        return items
    }
}
