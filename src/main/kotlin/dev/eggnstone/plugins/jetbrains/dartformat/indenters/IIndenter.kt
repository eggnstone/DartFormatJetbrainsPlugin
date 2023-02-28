package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

interface IIndenter
{
    fun indentPart(part: IPart): String
}
