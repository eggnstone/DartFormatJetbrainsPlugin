package com.eggnstone.jetbrainsplugins.dartformat.tokenizers

import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken

class WhiteSpaceTokenizer
{
    fun tokenize(input: String): ArrayList<IToken>
    {
        val outputTokens = arrayListOf<IToken>()

        var currentText = ""
        var isInInWhiteSpace = false
        for (currentChar in input)
        {
            if (isInInWhiteSpace)
            {
                if (Tools.isWhiteSpace(currentChar))
                {
                    currentText += currentChar
                    continue
                }

                if (currentText.isNotEmpty())
                    outputTokens += WhiteSpaceToken(currentText)

                currentText = currentChar.toString()
                isInInWhiteSpace = false
                continue
            }

            if (Tools.isWhiteSpace(currentChar))
            {
                if (currentText.isNotEmpty())
                    outputTokens += UnknownToken(currentText)

                currentText = currentChar.toString()
                isInInWhiteSpace = true
                continue
            }

            currentText += currentChar
        }

        if (currentText.isNotEmpty())
        {
            if (isInInWhiteSpace)
                outputTokens += WhiteSpaceToken(currentText)
            else
                outputTokens += UnknownToken(currentText)
        }

        return outputTokens
    }
}