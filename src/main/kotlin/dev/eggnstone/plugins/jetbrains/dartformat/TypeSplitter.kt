package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

data class SplitType(val name: String, val function: (String) -> Boolean, val combineSame: Boolean)

class TypeSplitter
{
    companion object
    {
        val types = listOf(
            SplitType("Bracket", Tools::isBracket, false),
            SplitType("Whitespace", Tools::isWhitespace, true)
        )
    }

    fun split(s: String): List<String>
    {
        val items = mutableListOf<String>()

        var currentText = ""
        var currentType: SplitType? = null

        @Suppress("ReplaceManualRangeWithIndicesCalls")
        for (i in 0 until s.length)
        {
            @Suppress("ReplaceGetOrSet")
            val c = s.get(i).toString()

            if (currentType != null)
            {
                if (currentType.function(c))
                {
                    if (currentType.combineSame)
                    {
                        currentText += c
                        continue
                    }

                    if (DotlinTools.isNotEmpty(currentText))
                        items.add(currentText)

                    currentText = c
                    continue
                }

                currentType = null

                if (DotlinTools.isNotEmpty(currentText))
                    items.add(currentText)

                currentText = ""
            }

            for (type in types)
            {
                if (type.function(c))
                {
                    currentType = type

                    if (DotlinTools.isNotEmpty(currentText))
                        items.add(currentText)

                    currentText = ""

                    break
                }
            }

            currentText += c
        }

        if (DotlinTools.isNotEmpty(currentText))
            items.add(currentText)

        //DotlinLogger.log("TypeSplitter(${Tools.toDisplayString(s)}) -> ${Tools.toDisplayStringForStrings(items)}")
        return items
    }
}
