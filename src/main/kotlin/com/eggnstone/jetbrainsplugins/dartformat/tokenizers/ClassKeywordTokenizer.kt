package com.eggnstone.jetbrainsplugins.dartformat.tokenizers

import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld
import com.eggnstone.jetbrainsplugins.dartformat.tokens.ClassKeywordToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken

class ClassKeywordTokenizer
{
    fun tokenize(input: String): MutableList<IToken>
    {
        val outputTokens = mutableListOf<IToken>()

        //DotlinTools.println("ClassKeywordTokenizer: $input")

        var currentText = ""
        for (currentChar in input)
        {
            if (currentText.endsWith(" "))
            {
                val pureText = currentText.substring(0, currentText.length - 1)
                if (ToolsOld.classKeywords.contains(pureText))
                {
                    outputTokens.add(ClassKeywordToken(pureText)) // dotlin
                    currentText = " "
                }
            }

            currentText += currentChar
        }

        if (ToolsOld.classKeywords.contains(currentText))
            outputTokens.add(ClassKeywordToken(currentText)) // dotlin
        else
            outputTokens.add(UnknownToken(currentText)) // dotlin

        return outputTokens
    }
}
