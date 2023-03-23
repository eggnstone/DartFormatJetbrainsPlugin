package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.BlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class DoubleBlockIndenter(private val spacesPerLevel: Int) : IIndenter
{
    override fun indentPart(part: IPart): String
    {
        if (part !is DoubleBlock)
            throw DartFormatException("Unexpected non-DoubleBlock type.")

        val doubleBlock: DoubleBlock = part

        //if (DotlinLogger.isEnabled) DotlinLogger.log("parts1: ${Tools.toDisplayStringForParts(doubleBlock.parts1)}")
        //if (DotlinLogger.isEnabled) DotlinLogger.log("parts2: ${Tools.toDisplayStringForParts(doubleBlock.parts2)}")

        val blockIndenter = BlockIndenter(spacesPerLevel)
        val indentedBody1 = blockIndenter.indentParts(doubleBlock.parts1, spacesPerLevel)
        val indentedBody2 = blockIndenter.indentParts(doubleBlock.parts2, spacesPerLevel)

        @Suppress("UnnecessaryVariable")
        val result = doubleBlock.header + indentedBody1 + doubleBlock.middle + indentedBody2 + doubleBlock.footer

        return result
    }
}
