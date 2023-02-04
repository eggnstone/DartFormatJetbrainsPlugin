package com.eggnstone.jetbrainsplugins.dartformat

class Tokenizer
{
    companion object
    {
        fun tokenize(input: String): ArrayList<IToken>
        {
            val tokens = arrayListOf<IToken>()

            var currentText = ""
            for (c in input)
            {
                if (c in 'a'..'z' || c in 'a'..'z')
                {
                    currentText += c
                }
                else
                {
                    if (currentText.isNotEmpty())
                    {
                        tokens += TextToken(currentText)
                        currentText = ""
                    }

                    tokens += DelimiterToken(c.toString())
                }
            }

            if (currentText.isNotEmpty())
                tokens += TextToken(currentText)

            return tokens
        }
    }
}
