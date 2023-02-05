package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

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
            if (TokenizerTools.isSpecial(currentChar))
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
