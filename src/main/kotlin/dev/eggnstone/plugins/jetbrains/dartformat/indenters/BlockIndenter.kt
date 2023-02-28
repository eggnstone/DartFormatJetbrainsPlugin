package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock

class SingleBlockIndenter : IIndenter
{
    companion object
    {
        private val masterIndenter = MasterIndenter()
    }

    override fun indentPart(part: IPart): String
    {
        if (part !is SingleBlock)
            throw DartFormatException("Unexpected non-SingleBlock type.")

        val singleBlock: SingleBlock = part

        @Suppress("UnnecessaryVariable")
        val result = singleBlock.header + masterIndenter.indentParts(singleBlock.parts) + singleBlock.footer

        return result
    }
}
