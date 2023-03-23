package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.LineSplitter

class BlockIndenter(spacesPerLevel: Int)
{
    companion object
    {
        private val lineSplitter = LineSplitter()
    }

    private var masterIndenter = MasterIndenter(spacesPerLevel)

    fun indentParts(parts: List<IPart>, spacesPerLevel: Int): String
    {
        //if (DotlinLogger.isEnabled) DotlinLogger.log("BlockIndenter.indentParts(${Tools.toDisplayStringForParts(parts)})")

        val body = masterIndenter.indentParts(parts)
        //if (DotlinLogger.isEnabled) DotlinLogger.log("BlockIndenter.indentParts: body: ${Tools.toDisplayString(body)}")
        val lines = lineSplitter.split(body, trim = false)

        //if (DotlinLogger.isEnabled) DotlinLogger.log("  Lines :${lines.size}")
        var indentedBody = ""
        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until lines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val line = lines.get(i) // workaround for dotlin
            //if (DotlinLogger.isEnabled) DotlinLogger.log("  Line #$i: ${Tools.toDisplayString(line)}")
            //val pad = if (DotlinTools.isBlank(line)) "" else "A"+DotlinTools.getSpaces(spacesPerLevel) +"X"
            val pad = if (DotlinTools.isBlank(line)) "" else DotlinTools.getSpaces(spacesPerLevel)
            indentedBody += pad + line
        }

        //if (DotlinLogger.isEnabled) DotlinLogger.log("BlockIndenter.indentParts: indentedBody: ${Tools.toDisplayString(indentedBody)}")
        return indentedBody
    }
}
