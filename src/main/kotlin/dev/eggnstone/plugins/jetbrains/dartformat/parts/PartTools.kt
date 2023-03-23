package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools

class PartTools
{
    companion object
    {
        fun printParts(parts: List<IPart>, label: String = "")
        {
            if (!Constants.DEBUG)
                return

            val prefix = if (DotlinTools.isEmpty(label)) "" else "$label - "

            if (DotlinTools.isEmpty(parts))
                DotlinLogger.log("${prefix}No parts.")
            else
                DotlinLogger.log("$prefix${parts.size} parts:")

            for (part in parts)
                DotlinLogger.log("  $part")
        }
    }
}
