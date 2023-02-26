package com.eggnstone.jetbrainsplugins.dartformat.tokenizers

import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld
import com.eggnstone.jetbrainsplugins.dartformat.tokens.EndOfLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException

class CommentTokenizer
{
    fun tokenize(input: String): MutableList<IToken>
    {
        if (ToolsOld.containsLineBreak(input))
            throw DartFormatException("CommentTokenizer.tokenize() must not be fed line breaks.")

        val outputTokens = mutableListOf<IToken>()

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
                        outputTokens += UnknownToken(currentText)
                        currentText = ""
                    }

                    isInEolComment = true
                    continue
                }

                if (nextChar == '*')
                {
                    if (currentText.isNotEmpty())
                    {
                        outputTokens += UnknownToken(currentText)
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
                outputTokens += MultiLineCommentToken(currentText.substring(1), isClosed = false)
            else
                outputTokens += UnknownToken(currentText)
        }

        return outputTokens
    }
}
