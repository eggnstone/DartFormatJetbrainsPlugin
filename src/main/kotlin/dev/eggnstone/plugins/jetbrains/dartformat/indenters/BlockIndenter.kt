package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.LineSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock

class SingleBlockIndenter : IIndenter
{
    companion object
    {
        private val masterIndenter = MasterIndenter()
        private val lineSplitter = LineSplitter()
    }

    override fun indentPart(part: IPart): String
    {
        if (part !is SingleBlock)
            throw DartFormatException("Unexpected non-SingleBlock type.")

        val singleBlock: SingleBlock = part

        DotlinLogger.log("parts: ${Tools.toDisplayStringForParts(singleBlock.parts)}")
        val body = masterIndenter.indentParts(singleBlock.parts)
        DotlinLogger.log("body: ${Tools.toDisplayString(body)}")

        val lines = lineSplitter.split(body)
        DotlinLogger.log("  Lines :${lines.size}")
        var indentedBody = ""
        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until lines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val line = lines.get(i) // workaround for dotlin
            DotlinLogger.log("  Line #$i: ${Tools.toDisplayString(line)}")
            if (line.isBlank())
            {
                indentedBody += line
                //indentedBody += "1" + DotlinTools.getSpaces(4) + "2" + line + "3"
            }
            else
                indentedBody += DotlinTools.getSpaces(4) + line
            //indentedBody +=  "1"+DotlinTools.getSpaces(4) + "2"+line+"3"
        }

        @Suppress("UnnecessaryVariable")
        val result = singleBlock.header + indentedBody + singleBlock.footer

        return result
    }
}
