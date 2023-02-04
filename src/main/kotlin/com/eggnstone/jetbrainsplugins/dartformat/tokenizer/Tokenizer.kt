package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.*

class Tokenizer
{
    companion object
    {
        fun tokenize(input: String): ArrayList<IToken>
        {
            val tokens = arrayListOf<IToken>()

            var isInMultiLineComment = false
            var isInEolComment = false
            var currentText = ""
            for ((index, c) in input.withIndex())
            {
                val previousC = if (index > 0) input[index - 1] else null
                val nextC = if (index < input.length - 1) input[index + 1] else null

                // end of line comment

                if (isInEolComment)
                {
                    if (c == '\n')
                    {
                        tokens += EndOfLineCommentToken(currentText.substring(1) + '\n')
                        currentText = ""
                        isInEolComment = false
                        continue
                    }

                    currentText += c
                    continue
                }

                // multi line comment

                if (isInMultiLineComment)
                {
                    if (c == '/')
                    {
                        if (previousC == '*')
                        {
                            tokens += MultiLineCommentToken(currentText.substring(1, currentText.length - 1))
                            currentText = ""
                            isInMultiLineComment = false
                            continue
                        }
                    }

                    currentText += c
                    continue
                }

                // start of end of line / multi line comment

                if (c == '/')
                {
                    if (nextC == '/')
                    {
                        if (currentText.isNotEmpty())
                        {
                            tokens += TextToken(currentText)
                            currentText = ""
                        }

                        isInEolComment = true
                        continue
                    }

                    if (nextC == '*')
                    {
                        if (currentText.isNotEmpty())
                        {
                            tokens += TextToken(currentText)
                            currentText = ""
                        }

                        isInMultiLineComment = true
                        continue
                    }
                }

                //

                if (c in 'a'..'z' || c in 'a'..'z')
                {
                    currentText += c
                }
                else
                {
                    if (currentText.isNotEmpty())
                    {
                        tokens += TextToken(currentText)
                        currentText = ""
                    }

                    tokens += DelimiterToken(c.toString())
                }
            }

            if (currentText.isNotEmpty())
            {
                if (isInEolComment)
                    tokens += EndOfLineCommentToken(currentText.substring(1))
                else if (isInMultiLineComment)
                    throw Exception()
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
    }
}
