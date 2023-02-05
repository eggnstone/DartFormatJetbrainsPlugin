package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.*

class TokenizerOld
{
    fun tokenize(input: String): ArrayList<IToken>
    {
        val tokens = arrayListOf<IToken>()

        var isInEolComment = false
        var isInMultiLineComment = false
        var isInWhiteSpace = false
        var isInText = true
        var currentText = ""
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

                if (currentChar == '\n' || nextChar == '\r')
                {
                    tokens += EndOfLineCommentToken(currentText.substring(1))
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
                    tokens += MultiLineCommentToken(currentText.substring(1, currentText.length - 2))
                    currentText = ""
                    isInMultiLineComment = false
                }

                continue
            }

            if (isInWhiteSpace)
            {
                if (isWhiteSpace(currentChar))
                {
                    currentText += currentChar
                    continue
                }

                if (currentText.isNotEmpty())
                {
                    tokens += WhiteSpaceToken(currentText)
                    currentText = ""
                }

                isInWhiteSpace = false
            }

            // start of end of line / multi line comment
            if (currentChar == '/')
            {
                if (nextChar == '/')
                {
                    if (isInText)
                    {
                        if (currentText.isNotEmpty())
                        {
                            tokens += TextToken(currentText)
                            currentText = ""
                        }
                    }
                    else
                        throw Exception("Unhandled type.")

                    isInText = false
                    isInEolComment = true
                    continue
                }

                if (nextChar == '*')
                {
                    if (isInText)
                    {
                        if (currentText.isNotEmpty())
                        {
                            tokens += TextToken(currentText)
                            currentText = ""
                        }
                    }
                    else
                        throw Exception("Unhandled type.")

                    isInText = false
                    isInMultiLineComment = true
                    continue
                }
            }

            if (isText(currentChar))
            {
                currentText += currentChar
                isInText = true
                continue
            }

            // Must be a delimiter

            if (currentText.isNotEmpty())
            {
                tokens += TextToken(currentText)
                currentText = ""
            }

            if (tokens.isEmpty())
            {
                tokens += DelimiterToken(currentChar.toString())
                continue
            }

            val lastToken = tokens.last()
            if (lastToken is WhiteSpaceToken)
            {
                if (lastToken.text == "\r" && currentChar == '\n')
                {
                    tokens.removeLast()
                    tokens += WhiteSpaceToken("\r\n")
                    continue
                }

                if (lastToken.text == "\n" && currentChar == '\r')
                {
                    tokens.removeLast()
                    tokens += WhiteSpaceToken("\n\r")
                    continue
                }
            }

            tokens += WhiteSpaceToken(currentChar.toString())
        }

        if (currentText.isNotEmpty())
        {
            if (isInEolComment)
                tokens += EndOfLineCommentToken(currentText.substring(1))
            else if (isInMultiLineComment)
            {
                TODO()
                throw Exception()
            }
            else
                tokens += TextToken(currentText)
        }

        return tokens
    }

    fun recreate(tokens: ArrayList<IToken>): String
    {
        val sb = StringBuilder()

        for (token in tokens)
            sb.append(token.recreate())

        return sb.toString()
    }

    private fun isText(currentChar: Char): Boolean = "_".contains(currentChar)
            || currentChar in 'a'..'z'
            || currentChar in 'A'..'Z'
            || currentChar in '0'..'9'

    private fun isWhiteSpace(currentChar: Char): Boolean = "\n\r\t ".contains(currentChar)
}
