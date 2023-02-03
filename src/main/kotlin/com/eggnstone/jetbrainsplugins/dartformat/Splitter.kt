package com.eggnstone.jetbrainsplugins.dartformat

class Splitter
{
    companion object
    {
        fun splitString(input: String): ArrayList<Part>
        {
            var parts = arrayListOf(Part(input, null))

            parts = splitParts(parts, '\n');
            parts = splitParts(parts, '(')
            parts = splitParts(parts, ')')
            parts = splitParts(parts, ',')
            parts = splitParts(parts, ' ')

            return parts
        }

        private fun splitParts(inParts: ArrayList<Part>, delimiter: Char): ArrayList<Part>
        {
            var outParts = arrayListOf<Part>()

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
