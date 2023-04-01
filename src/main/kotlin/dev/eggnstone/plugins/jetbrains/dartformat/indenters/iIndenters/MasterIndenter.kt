package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.parts.*
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

class MasterIndenter(private val spacesPerLevel: Int) : IIndenter
{
    override fun indentPart(part: IPart, startIndent: Int, indentLevel: Int): String
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("MasterIndenter.indentPart: $part")

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

        var isInSwitch = false
        var isInCaseOrDefault = false

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until parts.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val part = parts.get(i) // workaround for dotlin

            var indentedPart = indentPart(part, currentStartIndent)

            var caseOrDefaultEndPos = Tools.getTextEndPos(indentedPart, "case")
            if (caseOrDefaultEndPos < 0)
                caseOrDefaultEndPos = Tools.getTextEndPos(indentedPart, "default")

            if (caseOrDefaultEndPos < 0)
            {
                if (isInCaseOrDefault)
                    isInCaseOrDefault = false
            }
            else
            {
                isInSwitch = true
                if (isInCaseOrDefault)
                    throw DartFormatException("caseOrDefaultEndPos >= 0 && isInCaseOrDefault")

                isInCaseOrDefault = true
            }

            if (DotlinLogger.isEnabled) DotlinLogger.log("MasterIndenter.indentParts: Result of part #$i: $part")
            if (DotlinLogger.isEnabled) DotlinLogger.log("  indentedPart:           ${Tools.toDisplayStringShort(indentedPart)}")

            if (isInSwitch)
            {
                if (!isInCaseOrDefault && part !is Whitespace)
                    indentedPart = StringWrapper.getSpaces(spacesPerLevel) + indentedPart

                if (DotlinLogger.isEnabled) DotlinLogger.log("  corrected indentedPart: ${Tools.toDisplayStringShort(indentedPart)}")
            }

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

            result += indentedPart
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
