package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.*

class MasterIndenter(private val spacesPerLevel: Int) : IIndenter
{
    override fun indentPart(part: IPart, startIndent: Int, indentLevel: Int): String
    {
        //if (DotlinLogger.isEnabled) DotlinLogger.log("MasterIndenter.indentPart: $part")

        val indenter = getIndenter(part)
        return indenter.indentPart(part, startIndent, indentLevel)
    }

    fun indentParts(parts: List<IPart>): String
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("MasterIndenter.indentParts: ${Tools.toDisplayStringForParts(parts)}")

        if (DotlinTools.isEmpty(parts))
            return ""

        var currentStartIndent = 0
        var result = ""

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until parts.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val part = parts.get(i) // workaround for dotlin

            val indentedPart = indentPart(part, currentStartIndent)
            result += indentedPart

            if (DotlinLogger.isEnabled) DotlinLogger.log("MasterIndenter.indentParts: Result of part #$i: $part")
            if (DotlinLogger.isEnabled) DotlinLogger.log("  indentedPart:           ${Tools.toDisplayStringShort(indentedPart)}")
            val lastLine = Tools.getLastLine(indentedPart)
            if (DotlinLogger.isEnabled) DotlinLogger.log("  lastLine:               ${Tools.toDisplayStringShort(lastLine)}")
            val lastLineLength = lastLine.length
            if (DotlinLogger.isEnabled) DotlinLogger.log("  lastLineLength:         $lastLineLength")

            if (DotlinLogger.isEnabled) DotlinLogger.log("  old currentStartIndent: $currentStartIndent")
            if (indentedPart.contains("\n") || indentedPart.contains("\r"))
                currentStartIndent = lastLineLength
            else
                currentStartIndent += lastLineLength
            if (DotlinLogger.isEnabled) DotlinLogger.log("  new currentStartIndent: $currentStartIndent")
        }

        return result
    }

    fun getIndenter(inputPart: IPart): IIndenter
    {
        @Suppress("LiftReturnOrAssignment")
        when (inputPart)
        {
            is Comment -> return CommentIndenter(spacesPerLevel)
            is MultiBlock -> return MultiBlockIndenter(spacesPerLevel)
            is Statement -> return StatementIndenter(spacesPerLevel)
            is Whitespace -> return WhitespaceIndenter()
            else -> TODO("IPart not implemented yet: ${inputPart::class.simpleName}")
        }
    }
}
