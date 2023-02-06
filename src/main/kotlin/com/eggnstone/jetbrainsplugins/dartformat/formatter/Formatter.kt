package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.config.DartFormatConfig
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken

class Formatter(private val config: DartFormatConfig)
{
    fun format(inputTokens: ArrayList<IToken>): ArrayList<IToken>
    {
        var outputTokens = inputTokens

        if (config.removeUnnecessaryCommas)
            outputTokens = RemoveUnnecessaryCommasFormatter().format(outputTokens)

        return outputTokens
    }
}
