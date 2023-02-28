package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class DoubleBlockIndenter : IIndenter
{
    override fun indentPart(part: IPart): String
    {
        if (part !is DoubleBlock)
            throw DartFormatException("Unexpected non-DoubleBlock type.")

        return part.recreate()
    }
}
