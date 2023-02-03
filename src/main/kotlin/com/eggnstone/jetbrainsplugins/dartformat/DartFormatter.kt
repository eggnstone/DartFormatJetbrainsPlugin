package com.eggnstone.jetbrainsplugins.dartformat

class DartFormatter
{
    companion object
    {
        fun format(input: String): String
        {
            val output = StringBuilder()

            val parts = Splitter.splitString(input)
            for (i in parts.indices)
            {
                val previousPart = if (i > 0) parts[i - 1] else null
                val currentPart = parts[i]
                val nextPart = if (i < parts.size - 1) parts[i + 1] else null

                if (nextPart != null)
                {
                    if (currentPart.delimiter == ',' && nextPart.text.isEmpty() && nextPart.delimiter == ')')
                    {
                        // Ignore comma
                        println(currentPart.text)
                        output.append(currentPart.text)
                    }
                    else
                    {
                        println(currentPart.toString())
                        output.append(currentPart.recreate())
                    }
                }
                else
                {
                    println(currentPart.toString())
                    output.append(currentPart.recreate())
                }
            }

            return output.toString()
        }
    }
}
