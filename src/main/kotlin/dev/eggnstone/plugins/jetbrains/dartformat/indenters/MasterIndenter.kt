package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.*

class MasterIndenter(private val spacesPerLevel: Int) : IIndenter
{
    override fun indentPart(part: IPart): String
    {
        //DotlinLogger.log("MasterIndenter.indentPart: $part")

        val indenter = getIndenter(part)
        return indenter.indentPart(part)
    }

    fun indentParts(parts: List<IPart>): String
    {
        if (DotlinTools.isEmpty(parts))
            return ""

        var result = ""

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in 0 until parts.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val part = parts.get(i) // workaround for dotlin
            result += indentPart(part)
        }

        return result
    }

    fun getIndenter(inputPart: IPart): IIndenter
    {
        @Suppress("LiftReturnOrAssignment")
        when (inputPart)
        {
            is DoubleBlock -> return DoubleBlockIndenter(spacesPerLevel)
            is SingleBlock -> return SingleBlockIndenter(spacesPerLevel)
            is Statement -> return StatementIndenter(spacesPerLevel)
            is Whitespace -> return WhitespaceIndenter(spacesPerLevel)
            else -> TODO()
        }
    }
}
