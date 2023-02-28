package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace

class WhitespaceIndenter : IIndenter
{
    override fun indentPart(part: IPart): String
    {
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

        return result
    }
}
