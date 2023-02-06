package com.eggnstone.jetbrainsplugins.dartformat.formatters

import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import com.jetbrains.rd.framework.base.deepClonePolymorphic

class RemoveUnnecessaryCommasFormatter
{
    fun format(inputTokens: ArrayList<IToken>): ArrayList<IToken>
    {
        val outputTokens = inputTokens.deepClonePolymorphic()

        for (currentIndex in outputTokens.size - 1 downTo 0)
        {
            val currentToken = outputTokens[currentIndex]
            if (currentToken != SpecialToken.COMMA)
            {
                continue
            }

            for (nextIndex in currentIndex + 1 until outputTokens.size)
            {
                val nextToken = outputTokens[nextIndex]

                if (nextToken is EndOfLineCommentToken
                    || nextToken is LineBreakToken
                    || nextToken is MultiLineCommentToken
                    || nextToken is WhiteSpaceToken
                )
                {
                    continue
                }

                if (nextToken is SpecialToken && nextToken.isClosingBracket)
                {
                    outputTokens.removeAt(currentIndex)
                    break
                }

                break
            }
        }

        return outputTokens
    }
}
