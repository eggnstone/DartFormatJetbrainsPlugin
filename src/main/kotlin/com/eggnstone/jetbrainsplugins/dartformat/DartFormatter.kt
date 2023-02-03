package com.eggnstone.jetbrainsplugins.dartformat

class DartFormatter
{
    companion object
    {
        fun format(input: String): String
        {
            val output = StringBuilder()

            val parts = Splitter.splitString(input)
            for (i in parts.size - 1 downTo 0)
            {
                val previousPart = if (i > 0) parts[i - 1] else null
                val currentPart = parts[i]
                val nextPart = if (i < parts.size - 1) parts[i + 1] else null

                if (nextPart != null)
                {
                    if (currentPart.delimiter == ',' && nextPart.text.isEmpty() && nextPart.delimiter == ')')
                    {
                        if (currentPart.text.isEmpty())
                            parts.removeAt(i)
                        else
                        {
                            TODO("no tests yet!")
                            currentPart.delimiter = null
                        }
                    }
                }
            }

            for (part in parts)
                output.append(part.recreate())

            return output.toString()
        }
    }
}
