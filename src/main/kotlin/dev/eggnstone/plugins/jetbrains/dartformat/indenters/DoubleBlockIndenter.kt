package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class DoubleBlockIndenter : IIndenter
{
    override fun indentPart(part: IPart): String
    {
        if (part !is DoubleBlock)
            throw DartFormatException("Unexpected non-DoubleBlock type.")

        val doubleBlock: DoubleBlock = part

        DotlinLogger.log("parts1: ${Tools.toDisplayStringForParts(doubleBlock.parts1)}")
        DotlinLogger.log("parts2: ${Tools.toDisplayStringForParts(doubleBlock.parts2)}")

        val indentedBody1 = BlockIndenter().indentParts(doubleBlock.parts1)
        val indentedBody2 = BlockIndenter().indentParts(doubleBlock.parts2)

        @Suppress("UnnecessaryVariable")
        val result = doubleBlock.header + indentedBody1 + doubleBlock.middle + indentedBody2 + doubleBlock.footer

        return result
    }
}
