package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

interface IIndenter
{
    fun indentPart(part: IPart, currentLevel: Int = 0): String
}
