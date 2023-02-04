package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.DelimiterToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken

class Tokenizer
{
    companion object
    {
        fun tokenize(input: String): ArrayList<IToken>
        {
            val tokens = arrayListOf<IToken>()

            var isInMultilineComment = false
            var isInEolComment = false
            var currentText = ""
            for ((index, c) in input.withIndex())
            {
                val previousC = if (index > 0) input[index - 1] else null
                val nextC = if (index < input.length - 1) input[index + 1] else null

                if (isInMultilineComment)
                {
                    if (c == '/')
                    {
                        if (previousC == '*')
                        {
                            tokens += MultiLineToken(currentText.substring(1, currentText.length - 1))
                            currentText = ""
                            isInMultilineComment = false
                            continue
                        }
                    }

                    currentText += c
                    continue
                }

                if (c == '/')
                {
                    if (nextC == '*')
                    {
                        if (currentText.isNotEmpty())
                        {
                            tokens += TextToken(currentText)
                            currentText = ""
                        }

                        isInMultilineComment = true
                        continue
                    }
                }

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
                tokens += TextToken(currentText)

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
