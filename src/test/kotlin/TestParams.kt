class TestParams
{
    companion object
    {
        val brackets = arrayOf(
                arrayOf("{", "}", "Angle brackets"),
                arrayOf("(", ")", "Round brackets"),
                arrayOf("[", "]", "Square brackets")
        )

        val keywords = arrayOf(
                "do",
                "for",
                "if",
                "while"
        )

        val lineBreaks = arrayOf(
                arrayOf("\n", "\\n"),
                arrayOf("\n\r", "\\n\\r"),
                arrayOf("\r", "\\r"),
                arrayOf("\r\n", "\\r\\n")
        )

        val lineBreaksAndBrackets = join(lineBreaks, brackets)

        val lineBreaksAndKeywords = join2(lineBreaks, keywords)

        val quotes = arrayOf(
                arrayOf("\"", "'", "Normal quotes"),
                arrayOf("'", "\"", "Apostrophe")
        )

        private fun join(array1: Array<Array<String>>, array2: Array<Array<String>>): Array<Array<String>>
        {
            var result = arrayOf<Array<String>>()

            for (a1 in array1)
            {
                for (a2 in array2)
                {
                    var newArray = arrayOf<String>()

                    for (i1 in 0 until a1.size - 1)
                    {
                        val e1 = a1[i1]
                        newArray += e1
                    }

                    for (i2 in 0 until a2.size - 1)
                    {
                        val e2 = a2[i2]
                        newArray += e2
                    }

                    newArray += a1.last() + " + " + a2.last()

                    result += newArray
                }
            }

            return result
        }

        private fun join2(array1: Array<Array<String>>, array2: Array<String>): Array<Array<String>>
        {
            var result = arrayOf<Array<String>>()

            for (a1 in array1)
            {
                for (e2 in array2)
                {
                    var newArray = arrayOf<String>()

                    for (i1 in 0 until a1.size - 1)
                    {
                        val e1 = a1[i1]
                        newArray += e1
                    }

                    newArray += e2

                    newArray += a1.last() + " + " + e2

                    result += newArray
                }
            }

            return result
        }
    }
}
