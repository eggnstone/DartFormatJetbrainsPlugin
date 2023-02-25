package com.eggnstone.jetbrainsplugins.dartformat.tokenizers

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken

typealias TokenizeHandler = (String) -> MutableList<IToken>

class Tokenizer
{
    fun tokenize(input: String): MutableList<IToken>
    {
        var tokens = mutableListOf<IToken>(UnknownToken(input))

        // TODO: combine comments and string, because they can include each other

        // Line breaks, because they make the rest easier
        tokens = execute(LineBreakTokenizer()::tokenize, tokens)

        // Comments, because they overrule the rest
        tokens = execute(CommentTokenizer()::tokenize, tokens)

        // Strings, because they overrule the rest
        tokens = execute(StringTokenizer()::tokenize, tokens)

        // TODO: fix strings in comment or vice versa?

        // order not important
        tokens = execute(ClassKeywordTokenizer()::tokenize, tokens)
        tokens = execute(KeywordTokenizer()::tokenize, tokens)

        // order not important
        tokens = execute(SpecialTokenizer()::tokenize, tokens)
        tokens = execute(WhiteSpaceTokenizer()::tokenize, tokens)

        return tokens
    }

    private fun execute(f: TokenizeHandler, inputTokens: MutableList<IToken>): MutableList<IToken>
    {
        val outputTokens = mutableListOf<IToken>()

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