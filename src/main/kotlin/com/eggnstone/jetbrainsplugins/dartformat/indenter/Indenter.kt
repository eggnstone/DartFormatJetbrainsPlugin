package com.eggnstone.jetbrainsplugins.dartformat.indenter

import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken

class Indenter(private val spacesPerLevel: Int = 4)
{
    fun indent(tokens: ArrayList<IToken>): String
    {
        val sb = StringBuilder()

        var currentLineLevel = 0
        var currentText = ""
        var nextLineLevel = 0
        for (currentToken in tokens)
        {
            if (isOpeningBracket(currentToken))
            {
                currentText += currentToken.recreate()
                nextLineLevel++
                continue
            }

            if (isClosingBracket(currentToken))
            {
                // TODO: test for multiple brackets
                val reducedText = currentText.trim().replace(Regex("[})>\\]]"), "")
                if (reducedText.isEmpty())
                    currentLineLevel--

                currentText += currentToken.recreate()
                nextLineLevel--
                continue
            }

            if (currentToken is LineBreakToken)
            {
                sb.append(indent(currentText, currentLineLevel) + currentToken.recreate())

                currentLineLevel = nextLineLevel
                currentText = ""
                continue
            }

            if (currentText.isNotEmpty() || currentToken !is WhiteSpaceToken)
                currentText += currentToken.recreate()
        }

        if (currentText.isNotEmpty())
            sb.append(indent(currentText, currentLineLevel))

        return sb.toString()
    }

    fun recreate(tokens: ArrayList<IToken>): String
    {
        val sb = StringBuilder()

        for (token in tokens)
            sb.append(token.recreate())

        return sb.toString()
    }

    private fun indent(text: String, level: Int): String
    {
        if (Tools.containsLineBreak(text))
            throw Exception("containsLineBreak: ${Tools.toDisplayString(text)}")

        if (text.isBlank())
            return ""

        val pad = " ".repeat(level * spacesPerLevel)

        return pad + text
    }

    private fun isClosingBracket(currentToken: IToken): Boolean = currentToken is SpecialToken && currentToken.isClosingBracket

    private fun isOpeningBracket(currentToken: IToken): Boolean = currentToken is SpecialToken && currentToken.isOpeningBracket
}
