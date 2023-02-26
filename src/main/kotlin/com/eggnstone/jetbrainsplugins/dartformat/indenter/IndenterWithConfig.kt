package com.eggnstone.jetbrainsplugins.dartformat.indenter

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig

class IndenterWithConfig(private val config: DartFormatConfig)
{
    private val indenter = Indenter(config.indentationSpacesPerLevel)

    fun indent(tokens: MutableList<IToken>): String
    {
        if (!config.indentationIsEnabled)
            return indenter.recreate(tokens)

        return indenter.indent(tokens)
    }
}
