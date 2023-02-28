package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class SingleBlockIndenter : IIndenter
{
    override fun indentPart(part: IPart): String
    {
        return part.recreate()
    }
}
