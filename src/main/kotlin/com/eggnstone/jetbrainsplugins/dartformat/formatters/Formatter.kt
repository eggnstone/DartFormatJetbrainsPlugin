package com.eggnstone.jetbrainsplugins.dartformat.formatters

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken

class Formatter(
    private val removeUnnecessaryCommas: Boolean = true,
    private val removeUnnecessaryLineBreaksAfterArrows: Boolean = true
)
{
    fun format(inputTokens: ArrayList<IToken>): ArrayList<IToken>
    {
        var outputTokens = inputTokens

        if (removeUnnecessaryCommas)
            outputTokens = RemoveUnnecessaryCommasFormatter().format(outputTokens)

        if (removeUnnecessaryLineBreaksAfterArrows)
            outputTokens = RemoveUnnecessaryLineBreaksAfterArrows().format(outputTokens)

        return outputTokens
    }
}
