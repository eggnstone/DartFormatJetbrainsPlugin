package com.eggnstone.jetbrainsplugins.dartformat.tokenizers

import com.eggnstone.jetbrainsplugins.dartformat.Constants
import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken

class SpecialTokenizer
{
    fun tokenize(input: String): MutableList<IToken>
    {
        val outputTokens = mutableListOf<IToken>()

        var currentText = ""
        for ((index, currentChar) in input.withIndex())
        {
            val previousChar = if (index > 0) input[index - 1] else null
            val nextChar = if (index < input.length - 1) input[index + 1] else null

            if (Constants.EQUAL.equals(currentChar.toString()) && Constants.GREATER_THAN.equals(nextChar.toString()))
            {
                // ignore "=>" now to be treated next round
                continue
            }

            if (Constants.EQUAL.equals(previousChar.toString()) && Constants.GREATER_THAN.equals(currentChar.toString()))
            {
                if (currentText.isNotEmpty())
                    outputTokens += UnknownToken(currentText)//.substring(0, currentText.length - 1))

                currentText = ""
                outputTokens += SpecialToken.ARROW
                continue
            }

            if (ToolsOld.isSpecial(currentChar))
            {
                if (currentText.isNotEmpty())
                {
                    outputTokens += UnknownToken(currentText)
                    currentText = ""
                }

                outputTokens += SpecialToken(currentChar.toString())
                continue
            }

            currentText += currentChar
        }

        if (currentText.isNotEmpty())
            outputTokens += UnknownToken(currentText)

        return outputTokens
    }
}
