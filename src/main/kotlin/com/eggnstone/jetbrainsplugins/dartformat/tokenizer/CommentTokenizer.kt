package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken

class CommentTokenizer
{
    fun tokenize(input: String): ArrayList<IToken>
    {
        val outputTokens = arrayListOf<IToken>()

        var currentText = ""
        var isInMultiLineComment = false
        for ((index, currentChar) in input.withIndex())
        {
            val previousChar = if (index > 0) input[index - 1] else null
            val nextChar = if (index < input.length - 1) input[index + 1] else null

            if (isInMultiLineComment)
            {
                currentText += currentChar

                if (previousChar == '*' && currentChar == '/')
                {
                    outputTokens += MultiLineCommentToken(currentText.substring(1, currentText.length - 2))
                    currentText = ""
                    isInMultiLineComment = false
                }

                continue
            }

            if (currentChar == '/')
            {
                if (nextChar == '*')
                {
                    if (currentText.isNotEmpty())
                    {
                        outputTokens += TextToken(currentText)
                        currentText = ""
                    }

                    isInMultiLineComment = true
                    continue
                }
            }

            currentText += currentChar
        }

        if (currentText.isNotEmpty())
        {
            outputTokens += TextToken(currentText)
        }

        return outputTokens
    }
}
