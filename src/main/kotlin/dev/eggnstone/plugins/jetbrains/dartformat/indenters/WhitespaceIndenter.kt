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

        return ""
    }
}
