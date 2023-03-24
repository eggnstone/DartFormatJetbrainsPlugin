package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.*

class MasterIndenter(private val spacesPerLevel: Int) : IIndenter
{
    override fun indentPart(part: IPart, currentLevel: Int): String
    {
        //if (DotlinLogger.isEnabled) DotlinLogger.log("MasterIndenter.indentPart: $part")

        val indenter = getIndenter(part)
        return indenter.indentPart(part, currentLevel)
    }

    fun indentParts(parts: List<IPart>): String
    {
        //if (DotlinLogger.isEnabled) DotlinLogger.log("MasterIndenter.indentParts: $${Tools.toDisplayStringForParts(parts)}")

        if (DotlinTools.isEmpty(parts))
            return ""

        var currentLevel = 0
        var result = ""

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until parts.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val part = parts.get(i) // workaround for dotlin

            val indentedPart = indentPart(part, currentLevel)
            result += indentedPart

            val indentOfLastLine = Tools.getIndentOfLastLine(indentedPart)
            if (indentOfLastLine % spacesPerLevel != 0)
                throw DartFormatException("indentOfLastLine % spacesPerLevel != 0 (indentOfLastLine: $indentOfLastLine)")

            currentLevel = indentOfLastLine / spacesPerLevel
        }

        return result
    }

    fun getIndenter(inputPart: IPart): IIndenter
    {
        @Suppress("LiftReturnOrAssignment")
        when (inputPart)
        {
            is Comment -> return CommentIndenter(spacesPerLevel)
            is DoubleBlock -> return DoubleBlockIndenter(spacesPerLevel)
            is SingleBlock -> return SingleBlockIndenter(spacesPerLevel)
            is Statement -> return StatementIndenter(spacesPerLevel)
            is Whitespace -> return WhitespaceIndenter(spacesPerLevel)
            else -> TODO("IPart not implemented yet: ${inputPart::class.simpleName}")
        }
    }
}
