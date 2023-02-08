package com.eggnstone.jetbrainsplugins.dartformat.formatters

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken

class Formatter(
    private val removeUnnecessaryCommas: Boolean = true,
    private val removeLineBreaksAfterArrows: Boolean = true
)
{
    fun format(inputTokens: ArrayList<IToken>): ArrayList<IToken>
    {
        var outputTokens = inputTokens

        if (removeUnnecessaryCommas)
            outputTokens = RemoveUnnecessaryCommasFormatter().format(outputTokens)

        if (removeLineBreaksAfterArrows)
            outputTokens = RemoveLineBreaksAfterArrows().format(outputTokens)

        return outputTokens
    }
}
