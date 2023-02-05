package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.config.DartFormatPersistentStateComponent
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken

class Formatter
{
    fun format(inputTokens: ArrayList<IToken>): String
    {
        val output = StringBuilder()

        var outputTokens = inputTokens

        if (getRemoveUnnecessaryCommas())
            outputTokens = RemoveUnnecessaryCommasFormatter().format(outputTokens)

        for (outputToken in outputTokens)
            output.append(outputToken.recreate())

        return output.toString()
    }

    private fun getRemoveUnnecessaryCommas(): Boolean
    {
        if (DartFormatPersistentStateComponent.instance == null)
            return true

        return DartFormatPersistentStateComponent.instance!!.state.removeUnnecessaryCommas
    }
}
