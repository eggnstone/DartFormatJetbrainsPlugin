package com.eggnstone.jetbrainsplugins.dartformat.tokenizers

import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.ClassKeywordToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken

class ClassKeywordTokenizer
{
    fun tokenize(input: String): ArrayList<IToken>
    {
        val outputTokens = arrayListOf<IToken>()

        //println("ClassKeywordTokenizer: $input")

        var currentText = ""
        for (currentChar in input)
        {
            if (currentText.endsWith(" "))
            {
                val pureText = currentText.substring(0, currentText.length - 1)
                if (Tools.classKeywords.contains(pureText))
                {
                    outputTokens += ClassKeywordToken(pureText)
                    currentText = " "
                }
            }

            currentText += currentChar
        }

        if (Tools.classKeywords.contains(currentText))
            outputTokens += ClassKeywordToken(currentText)
        else
            outputTokens += UnknownToken(currentText)

        return outputTokens
    }
}
