package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger

class PartTools
{
    companion object
    {
        fun printParts(parts: List<IPart>)
        {
            for (part in parts)
                DotlinLogger.log(part.toString())
        }
    }
}
