package com.eggnstone.jetbrainsplugins.dartformat.tokenizers

import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.KeywordToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken

class KeywordTokenizer
{
    fun tokenize(input: String): MutableList<IToken>
    {
        val outputTokens = mutableListOf<IToken>()

        var currentText = ""
        var currentWord = ""
        for (currentChar in input)
        {
            if (Regex("[a-zA-Z0-9_]").matches(currentChar.toString()))
            {
                currentText += currentChar
                currentWord += currentChar
                continue
            }

            if (ToolsOld.isKeyword(currentWord))
            {
                currentText = currentText.substring(0, currentText.length - currentWord.length)
                if (currentText.isNotEmpty())
                    outputTokens += UnknownToken(currentText)

                outputTokens += KeywordToken(currentWord)
                currentText = ""
            }

            currentText += currentChar
            currentWord = ""
        }

        //DotlinLogger.log("Testing $currentWord")
        if (ToolsOld.isKeyword(currentWord))
        {
            currentText = currentText.substring(0, currentText.length - currentWord.length)
            if (currentText.isNotEmpty())
                outputTokens += UnknownToken(currentText)

            outputTokens += KeywordToken(currentWord)
        }
        else
            outputTokens += UnknownToken(currentText)

        return outputTokens
    }
}
