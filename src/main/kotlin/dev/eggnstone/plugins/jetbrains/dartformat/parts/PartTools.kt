package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger

class PartTools
{
    companion object
    {
        fun printParts(parts: List<IPart>)
        {
            DotlinLogger.log("${parts.size} parts:")
            for (part in parts)
                DotlinLogger.log("  $part")
        }
    }
}
