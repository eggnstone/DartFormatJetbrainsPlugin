package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken

class Formatter
{
    companion object
    {
        fun format(tokens: ArrayList<IToken>): String
        {
            val output = StringBuilder()

            for (currentIndex in tokens.size - 1 downTo 0)
            {
                //val previousToken = if (currentIndex > 0) tokens[currentIndex - 1] else null
                val currentToken = tokens[currentIndex]

                if (currentToken == SpecialToken.COMMA)
                {
                    //println("currentToken >$currentToken<")
                    for (nextIndex in currentIndex + 1 until tokens.size)
                    {
                        //println(">${tokens[nextIndex]}<")
                        val nextToken = tokens[nextIndex]

                        if (nextToken is WhiteSpaceToken)//  && nextToken.isNewLine)
                            continue

                        if (nextToken == SpecialToken.CLOSING_BRACKET)
                        {
                            //println("Removing ${tokens.size}")
                            tokens.removeAt(currentIndex)
                            //println("         ${tokens.size}")
                            break
                        }

                        if (nextToken is SpecialToken)
                        {
                            //println("Delimiter: \"${nextToken.delimiter}\"")
                            if (nextToken.text == " ")
                                continue
                        }

                        break
                    }
                }
            }

            for (token in tokens)
                output.append(token.recreate())

            return output.toString()
        }
    }
}
