package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.Tokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.DelimiterToken

class Formatter
{
    companion object
    {
        fun format(input: String): String
        {
            val output = StringBuilder()

            val tokens = Tokenizer.tokenize(input)
            for (i in tokens.size - 1 downTo 0)
            {
                val previousToken = if (i > 0) tokens[i - 1] else null
                val currentToken = tokens[i]
                val nextToken = if (i < tokens.size - 1) tokens[i + 1] else null

                if (nextToken != null)
                {
                    if (currentToken == DelimiterToken.COMMA && nextToken == DelimiterToken.CLOSING_BRACKET)
                        tokens.removeAt(i)
                }
            }

            for (token in tokens)
                output.append(token.recreate())

            return output.toString()
        }
    }
}
