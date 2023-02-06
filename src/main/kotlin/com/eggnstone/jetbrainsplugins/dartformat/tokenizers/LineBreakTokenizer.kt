package com.eggnstone.jetbrainsplugins.dartformat.tokenizers

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken

class LineBreakTokenizer
{
    fun tokenize(input: String): ArrayList<IToken>
    {
        val outputTokens = arrayListOf<IToken>()

        var currentText = ""
        for (currentChar in input)
        {
            val previousToken = if (outputTokens.size > 0) outputTokens[outputTokens.size - 1] else null

            if (currentChar == '\n')
            {
                if (previousToken == LineBreakToken.R)
                {
                    outputTokens.removeLast()
                    outputTokens += LineBreakToken.RN
                }
                else
                {
                    if (currentText.isNotEmpty())
                        outputTokens += UnknownToken(currentText)

                    outputTokens += LineBreakToken.N
                }

                currentText = ""
                continue
            }

            if (currentChar == '\r')
            {
                if (previousToken == LineBreakToken.N)
                {
                    outputTokens.removeLast()
                    outputTokens += LineBreakToken.NR
                }
                else
                {
                    if (currentText.isNotEmpty())
                        outputTokens += UnknownToken(currentText)

                    outputTokens += LineBreakToken.R
                }

                currentText = ""
                continue
            }

            currentText += currentChar
        }

        if (currentText.isNotEmpty())
            outputTokens += UnknownToken(currentText)

        return outputTokens
    }
}
