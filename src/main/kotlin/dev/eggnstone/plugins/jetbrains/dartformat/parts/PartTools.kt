package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger

class PartTools
{
    companion object
    {
        fun printParts(parts: List<IPart>, label: String = "")
        {
            @Suppress("ReplaceSizeZeroCheckWithIsEmpty") // workaround for dotlin for: isEmpty
            val prefix = if (label.length == 0) "" else "$label - "

            @Suppress("ReplaceSizeZeroCheckWithIsEmpty") // workaround for dotlin for: isEmpty
            if (parts.size == 0)
                DotlinLogger.log("${prefix}No parts.")
            else
                DotlinLogger.log("$prefix${parts.size} parts:")

            for (part in parts)
                DotlinLogger.log("  $part")
        }
    }
}
