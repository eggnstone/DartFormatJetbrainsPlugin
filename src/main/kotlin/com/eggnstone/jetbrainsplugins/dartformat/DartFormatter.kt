package com.eggnstone.jetbrainsplugins.dartformat

class DartFormatter
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

            println("${tokens.size} tokens:")
            for (token in tokens)
            {
                println(token)
                output.append(token)
            }

            return output.toString()
        }
    }
}
