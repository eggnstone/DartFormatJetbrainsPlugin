package com.eggnstone.jetbrainsplugins.dartformat.tokenizers

import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken

class SpecialTokenizer
{
    fun tokenize(input: String): ArrayList<IToken>
    {
        val outputTokens = arrayListOf<IToken>()

        var currentText = ""
        for ((index, currentChar) in input.withIndex())
        {
            val previousChar = if (index > 0) input[index - 1] else null
            val nextChar = if (index < input.length - 1) input[index + 1] else null

            if (currentChar == Tools.EQUAL_CHAR && nextChar == Tools.CLOSING_POINTY_BRACKET_CHAR)
            {
                // ignore "=>" now to be treated next round
                continue
            }

            if (previousChar == Tools.EQUAL_CHAR && currentChar == Tools.CLOSING_POINTY_BRACKET_CHAR)
            {
                if (currentText.isNotEmpty())
                    outputTokens += UnknownToken(currentText)//.substring(0, currentText.length - 1))

                currentText = ""
                outputTokens += SpecialToken.ARROW
                continue
            }

            if (Tools.isSpecial(currentChar))
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
