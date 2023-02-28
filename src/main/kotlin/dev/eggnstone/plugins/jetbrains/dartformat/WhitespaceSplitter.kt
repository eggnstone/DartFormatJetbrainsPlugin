package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

class WhitespaceSplitter
{
    fun split(s: String): List<String>
    {
        val items = mutableListOf<String>()

        var currentText = ""
        var isWhitespace = false

        @Suppress("ReplaceManualRangeWithIndicesCalls")
        for (i in 0 until s.length)
        {
            @Suppress("ReplaceGetOrSet")
            val c = s.get(i).toString()

            if (isWhitespace)
            {
                if (Tools.isWhitespace(c))
                {
                    currentText += c
                    continue
                }

                isWhitespace = false

                if (DotlinTools.isNotEmpty(currentText))
                    items.add(currentText)

                currentText = c
                continue
            }

            if (Tools.isWhitespace(c))
            {
                isWhitespace = true

                if (DotlinTools.isNotEmpty(currentText))
                    items.add(currentText)

                currentText = c
                continue
            }

            currentText += c
        }

        if (DotlinTools.isNotEmpty(currentText))
            items.add(currentText)

        return items
    }
}
