package com.eggnstone.jetbrainsplugins.dartformat.formatters

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig

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
