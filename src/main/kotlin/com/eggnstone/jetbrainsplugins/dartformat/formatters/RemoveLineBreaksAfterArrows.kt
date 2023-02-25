package com.eggnstone.jetbrainsplugins.dartformat.formatters

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import com.jetbrains.rd.framework.base.deepClonePolymorphic

class RemoveLineBreaksAfterArrows
{
    fun format(inputTokens: MutableList<IToken>): MutableList<IToken>
    {
        val outputTokens = inputTokens.deepClonePolymorphic()

        for (currentIndex in outputTokens.size - 1 downTo 0)
        {
            val currentToken = outputTokens[currentIndex]
            if (currentToken != SpecialToken.ARROW)
                continue

            val nextToken1 = if (currentIndex < outputTokens.size - 1) outputTokens[currentIndex + 1] else null
            val nextToken2 = if (currentIndex < outputTokens.size - 2) outputTokens[currentIndex + 2] else null
            val nextToken3 = if (currentIndex < outputTokens.size - 3) outputTokens[currentIndex + 3] else null

            if (nextToken1 is LineBreakToken)
            {
                // Remove line break
                outputTokens.removeAt(currentIndex + 1)

                // Remove white space
                if (nextToken2 is WhiteSpaceToken)
                    outputTokens[currentIndex + 1] = WhiteSpaceToken(" ")

                continue
            }

            if (nextToken1 is WhiteSpaceToken && nextToken2 is LineBreakToken)
            {
                // Remove white space
                outputTokens.removeAt(currentIndex + 1)

                // Remove line break
                outputTokens.removeAt(currentIndex + 1)

                // Remove white space
                if (nextToken3 is WhiteSpaceToken)
                    outputTokens[currentIndex + 1] = WhiteSpaceToken(" ")

                continue
            }
        }

        return outputTokens
    }
}
