package com.eggnstone.jetbrainsplugins.dartformat.indenter

import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken

class Indenter
{
    private val indentation = 4

    fun indent(tokens: ArrayList<IToken>): String
    {
        val sb = StringBuilder()

        var currentLineLevelRelative = 0
        var currentLineLevelDecreasedBeforeNonWhiteSpace = 0
        var currentLineLevel = 0
        var currentText = ""
        for (currentToken in tokens)
        {
            if (currentToken == SpecialToken.OPENING_ANGLE_BRACKET)
            {
                currentLineLevelRelative++
                currentText += currentToken.recreate()
                continue
            }

            if (currentToken == SpecialToken.CLOSING_ANGLE_BRACKET)
            {
                currentLineLevelRelative--
                if (currentText.trim().isEmpty())
                    currentLineLevelDecreasedBeforeNonWhiteSpace++

                currentText += currentToken.recreate()
                continue
            }

            if (currentToken is LineBreakToken)
            {
                sb.append(indent(currentText, currentLineLevel - currentLineLevelDecreasedBeforeNonWhiteSpace) + currentToken.recreate())

                currentText = ""
                currentLineLevel += currentLineLevelRelative
                currentLineLevelRelative = 0
                currentLineLevelDecreasedBeforeNonWhiteSpace = 0
                continue
            }

            if (currentText.isNotEmpty() || currentToken !is WhiteSpaceToken)
                currentText += currentToken.recreate()
        }

        if (currentText.isNotEmpty())
            sb.append(indent(currentText, currentLineLevel-currentLineLevelDecreasedBeforeNonWhiteSpace))

        return sb.toString()
    }

    private fun indent(text: String, level: Int): String
    {
        if (Tools.containsLineBreak(text))
            throw Exception("containsLineBreak: ${Tools.toDisplayString(text)}")

        if (text.isBlank())
            return ""

        val pad = " ".repeat(level * indentation)

        return pad + text
    }

    fun recreate(tokens: ArrayList<IToken>): String
    {
        val sb = StringBuilder()

        for (token in tokens)
            sb.append(token.recreate())

        return sb.toString()
    }
}
