package com.eggnstone.jetbrainsplugins.dartformat.formatters

import com.eggnstone.jetbrainsplugins.dartformat.config.DartFormatConfig
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken

class FormatterWithConfig(private val config: DartFormatConfig)
{
    fun format(tokens: MutableList<IToken>): MutableList<IToken>
    {
        val formatter = Formatter(
            removeUnnecessaryCommas = config.removeUnnecessaryCommas,
            removeLineBreaksAfterArrows = config.removeLineBreaksAfterArrows
        )

        return formatter.format(tokens)
    }
}
