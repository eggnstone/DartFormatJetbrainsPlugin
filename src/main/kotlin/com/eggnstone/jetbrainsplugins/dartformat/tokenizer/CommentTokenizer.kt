package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken

class CommentTokenizer
{
    fun tokenize(input: String): ArrayList<IToken>
    {
        val outputTokens = arrayListOf<IToken>()

        var currentText = ""
        var isInEolComment = false
        var isInMultiLineComment = false
        for ((index, currentChar) in input.withIndex())
        {
            val previousChar = if (index > 0) input[index - 1] else null
            val nextChar = if (index < input.length - 1) input[index + 1] else null

            if (isInEolComment)
            {
                currentText += currentChar

                if (currentChar == '\n' && nextChar == '\r')
                    continue

                if (currentChar == '\r' && nextChar == '\n')
                    continue

                if (currentChar == '\n' || currentChar == '\r')
                {
                    outputTokens += EndOfLineCommentToken(currentText.substring(1))
                    currentText = ""
                    isInEolComment = false
                }

                continue
            }

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
                if (nextChar == '/')
                {
                    if (currentText.isNotEmpty())
                    {
                        outputTokens += TextToken(currentText)
                        currentText = ""
                    }

                    isInEolComment = true
                    continue
                }

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
            if (isInEolComment)
                outputTokens += EndOfLineCommentToken(currentText.substring(1))
            else if (isInMultiLineComment)
                outputTokens += MultiLineCommentToken(currentText.substring(1, currentText.length), isClosed = false)
            else
                outputTokens += TextToken(currentText)
        }

        return outputTokens
    }
}
