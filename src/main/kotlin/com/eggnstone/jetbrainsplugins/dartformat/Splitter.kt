package com.eggnstone.jetbrainsplugins.dartformat

class Splitter
{
    companion object
    {
        fun splitString(input: String): Array<Part>
        {
            var parts = arrayOf<Part>(Part(input, null))

            parts = splitParts(parts, '\n');
            parts = splitParts(parts, '(')
            parts = splitParts(parts, ')')
            parts = splitParts(parts, ',')
            parts = splitParts(parts, ' ')

            return parts
        }

        private fun splitParts(inParts: Array<Part>, delimiter: Char): Array<Part>
        {
            var outParts = arrayOf<Part>()

            for (inPart in inParts)
            {
                val newTexts = inPart.text.split(delimiter)
                for (i in 0..newTexts.size - 2)
                {
                    val newText = newTexts[i]
                    outParts += Part(newText, delimiter)
                }

                outParts += Part(newTexts[newTexts.size - 1], inPart.delimiter)
            }

            return outParts
        }
    }
}
