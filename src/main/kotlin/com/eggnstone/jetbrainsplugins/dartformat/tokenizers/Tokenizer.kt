package com.eggnstone.jetbrainsplugins.dartformat.tokenizers

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken

typealias TokenizeHandler = (String) -> ArrayList<IToken>

class Tokenizer
{
    fun tokenize(input: String): ArrayList<IToken>
    {
        var tokens = arrayListOf<IToken>(UnknownToken(input))

        // 1. line breaks, because it makes the rest easier
        tokens = execute(LineBreakTokenizer()::tokenize, tokens)

        // 2. comments, because they overrule the rest
        tokens = execute(CommentTokenizer()::tokenize, tokens)

        // 3. string, because they overrule the rest
        tokens = execute(StringTokenizer()::tokenize, tokens)

        // order not important
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