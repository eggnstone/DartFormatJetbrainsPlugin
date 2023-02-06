package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.StringToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken

class StringTokenizer
{
    fun tokenize(input: String): ArrayList<IToken>
    {
        val outputTokens = arrayListOf<IToken>()

        var currentText = ""
        var isInNormalQuotesString = false
        var isInApostropheString = false
        for ((index, currentChar) in input.withIndex())
        {
            val previousChar = if (index > 0) input[index - 1] else null

            if (isInNormalQuotesString)
            {
                if (previousChar != '\\' && currentChar == '"')
                {
                    currentText += currentChar
                    outputTokens += StringToken(currentText)
                    currentText = ""
                    isInNormalQuotesString = false
                    continue
                }

                currentText += currentChar
                continue
            }

            if (isInApostropheString)
            {
                if (previousChar != '\\' && currentChar == '\'')
                {
                    currentText += currentChar
                    outputTokens += StringToken(currentText)
                    currentText = ""
                    isInApostropheString = false
                    continue
                }

                currentText += currentChar
                continue
            }

            if (previousChar != '\\' && currentChar == '"')
            {
                if (currentText.isNotEmpty())
                    outputTokens += UnknownToken(currentText)

                currentText = currentChar.toString()
                isInNormalQuotesString = true
                continue
            }

            if (previousChar != '\\' && currentChar == '\'')
            {
                if (currentText.isNotEmpty())
                    outputTokens += UnknownToken(currentText)

                currentText = currentChar.toString()
                isInApostropheString = true
                continue
            }

            currentText += currentChar
        }

        if (currentText.isNotEmpty())
        {
            if (isInNormalQuotesString || isInApostropheString)
                outputTokens += StringToken(currentText, isClosed = false)
            else
                outputTokens += UnknownToken(currentText)
        }

        return outputTokens
    }
}
