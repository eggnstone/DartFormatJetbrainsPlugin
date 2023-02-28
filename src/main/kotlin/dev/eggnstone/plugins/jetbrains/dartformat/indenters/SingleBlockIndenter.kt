package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock

class SingleBlockIndenter : IIndenter
{
    override fun indentPart(part: IPart): String
    {
        if (part !is SingleBlock)
            throw DartFormatException("Unexpected non-SingleBlock type.")

        val singleBlock: SingleBlock = part

        DotlinLogger.log("parts: ${Tools.toDisplayStringForParts(singleBlock.parts)}")

        val indentedBody = BlockIndenter().indentParts(singleBlock.parts)

        @Suppress("UnnecessaryVariable")
        val result = singleBlock.header + indentedBody + singleBlock.footer

        return result
    }
}
