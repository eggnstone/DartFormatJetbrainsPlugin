package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.LineSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class BlockIndenter
{
    companion object
    {
        private val masterIndenter = MasterIndenter()
        private val lineSplitter = LineSplitter()
    }

    fun indentParts(parts: List<IPart>): String
    {
        val body = masterIndenter.indentParts(parts)
        val lines = lineSplitter.split(body)

        //DotlinLogger.log("  Lines :${lines.size}")
        var indentedBody = ""
        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until lines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val line = lines.get(i) // workaround for dotlin
            //DotlinLogger.log("  Line #$i: ${Tools.toDisplayString(line)}")
            val pad = if (DotlinTools.isBlank(line)) "" else DotlinTools.getSpaces(4)
            indentedBody += pad + line
        }

        return indentedBody
    }
}
