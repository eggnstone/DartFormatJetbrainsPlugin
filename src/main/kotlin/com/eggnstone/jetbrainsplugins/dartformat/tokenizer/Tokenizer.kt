package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken

typealias TokenizeHandler = (String) -> ArrayList<IToken>

class Tokenizer
{
    fun recreate(tokens: ArrayList<IToken>): String
    {
        val sb = StringBuilder()

        for (token in tokens)
            sb.append(token.recreate())

        return sb.toString()
    }

    fun tokenize(input: String): ArrayList<IToken>
    {
        var tokens = arrayListOf<IToken>(UnknownToken(input))

        tokens = execute(CommentTokenizer()::tokenize, tokens)
        tokens = execute(StringTokenizer()::tokenize, tokens)
        tokens = execute(WhiteSpaceTokenizer()::tokenize, tokens)
        tokens = execute(SpecialTokenizer()::tokenize, tokens)

        return tokens
    }

    private fun execute(f: TokenizeHandler, inputTokens: ArrayList<IToken>): ArrayList<IToken>
    {
        val outputTokens = arrayListOf<IToken>()

        for (inputToken in inputTokens)
        {
            if (inputToken is UnknownToken)
                outputTokens += f(inputToken.text)
            else
                outputTokens += inputToken
        }

        return outputTokens
    }
}
