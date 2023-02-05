package com.eggnstone.jetbrainsplugins.dartformat.formatter

import com.eggnstone.jetbrainsplugins.dartformat.tokens.DelimiterToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken
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

                if (currentToken == DelimiterToken.COMMA)
                {
                    //println("currentToken >$currentToken<")
                    for (nextIndex in currentIndex + 1 until tokens.size)
                    {
                        //println("accessing ${tokens.size}")
                        // Necessary check because we modify the array.
                        // But Kotlin says that it's not necessary.
                        /*if (nextIndex >= tokens.size)
                            break*/

                        //println(">${tokens[nextIndex]}<")
                        val nextToken = tokens[nextIndex]

                        if (nextToken is WhiteSpaceToken)//  && nextToken.isNewline)
                            continue

                        if (nextToken == DelimiterToken.CLOSING_BRACKET)
                        {
                            //println("Removing ${tokens.size}")
                            tokens.removeAt(currentIndex)
                            //println("         ${tokens.size}")
                            break
                        }

                        //println("${nextToken::class}")

                        if (nextToken is TextToken)
                        {
                            //println(nextToken.text)
                            if (nextToken.text.trim().isEmpty())
                                continue
                        }

                        if (nextToken is DelimiterToken)
                        {
                            //println("Delimiter: \"${nextToken.delimiter}\"")
                            if (nextToken.delimiter == " ")
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
