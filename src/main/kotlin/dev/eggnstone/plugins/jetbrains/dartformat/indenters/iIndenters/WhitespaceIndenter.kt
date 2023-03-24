package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace

class WhitespaceIndenter : IIndenter
{
    override fun indentPart(part: IPart, startIndent: Int, indentLevel: Int): String
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("WhitespaceIndenter.indentPart: $part")

        if (part !is Whitespace)
            throw DartFormatException("Unexpected non-Whitespace type.")

        val whitespace: Whitespace = part

        var result = ""

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until whitespace.text.length) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val c = whitespace.text.get(i).toString() // workaround for dotlin

            if (c == "\n" || c == "\r")
                result += c
        }

        if (startIndent > 0)
        {
            //val leadingSpaces = Tools.countLeadingSpaces(result)
            if (StringWrapper.isEmpty(result))
                result = " "
        }

        return result
    }
}
