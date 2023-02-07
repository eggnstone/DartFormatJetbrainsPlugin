package com.eggnstone.jetbrainsplugins.dartformat.indenter

import com.eggnstone.jetbrainsplugins.dartformat.config.DartFormatConfig
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken

class IndenterWithConfig(private val config: DartFormatConfig)
{
    private val indenter = Indenter(config.indentationSpacesPerLevel)

    fun indent(tokens: ArrayList<IToken>): String
    {
        if (!config.indentationIsEnabled)
            return indenter.recreate(tokens)

        return indenter.indent(tokens)
    }
}
