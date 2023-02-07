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
        for (currentChar in input)
        {
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

            if (currentText.isNotEmpty())
            {
                val previousChar = currentText.last()
                if (Tools.isSpecial(currentChar, previousChar))
                {
                    outputTokens += UnknownToken(currentText.substring(0, currentText.length - 1))
                    currentText = ""

                    outputTokens += SpecialToken.ARROW
                    continue
                }
            }

            currentText += currentChar
        }

        if (currentText.isNotEmpty())
            outputTokens += UnknownToken(currentText)

        return outputTokens
    }
}
