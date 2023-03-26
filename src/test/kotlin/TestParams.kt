import dev.eggnstone.plugins.jetbrains.dartformat.Constants

class TestParams
{
    companion object
    {
        val brackets = arrayOf(
            arrayOf("{", "}", "Curly brackets"),
            arrayOf("(", ")", "Round brackets"),
            arrayOf("[", "]", "Square brackets")
        )

        val classKeywords = arrayOf(
            "abstract class",
            "class",
            "extends",
            "implements",
            "with"
        )

        val keywords = arrayOf(
            //"case", // ?
            "catch",
            "do",
            "else",
            "finally",
            "for",
            "if",
            "switch",
            "try",
            "while"
        )

        val lineBreaks = arrayOf(
            arrayOf("\n", "\\n"),
            arrayOf("\n\r", "\\n\\r"),
            arrayOf("\r", "\\r"),
            arrayOf("\r\n", "\\r\\n")
        )

        val lineBreaksAndBrackets = join(lineBreaks, brackets)

        val quotes = arrayOf(
            arrayOf("\"", "Normal quotes"),
            arrayOf("'", "Apostrophes")
        )

        val quotesWithInnerQuotes = arrayOf(
            arrayOf("\"", "'", "Normal quotes with inner apostrophes"),
            arrayOf("\"", "\\\"", "Normal quotes with inner escaped normal quotes"),
            arrayOf("'", "\"", "Apostrophes with inner normal quotes"),
            arrayOf("'", "\\'", "Apostrophes with inner escaped apostrophes")
        )

        val quotesWithOtherQuotes = arrayOf(
            arrayOf("\"", "'", "Normal quotes and apostrophes"),
            arrayOf("'", "\"", "Apostrophes and normal quotes")
        )

        val specials = arrayOf(
            Constants.OPENING_CURLY_BRACKET, Constants.CLOSING_CURLY_BRACKET,
            Constants.OPENING_ROUND_BRACKET, Constants.CLOSING_ROUND_BRACKET,
            Constants.OPENING_SQUARE_BRACKET, Constants.CLOSING_SQUARE_BRACKET,
            Constants.ARROW,
            Constants.COLON,
            Constants.COMMA,
            Constants.GREATER_THAN,
            Constants.PERIOD,
            Constants.SEMICOLON
        )

        private val statementOrBlockBooleans = arrayOf(
            arrayOf("false", "s();"),
            arrayOf("true", "{}")
        )

        val statementOrBlockBooleans2 = join(statementOrBlockBooleans, statementOrBlockBooleans)

        val statementOrBlockBooleans3 = join(statementOrBlockBooleans2, statementOrBlockBooleans)

        val whitespaces = arrayOf(
            arrayOf(" ", "Space"),
            arrayOf("\t", "Tab"),
            arrayOf("\n", "\\n"),
            arrayOf("\n\r", "\\n\\r"),
            arrayOf("\r", "\\r"),
            arrayOf("\r\n", "\\r\\n")
        )

        private fun join(arrayOfArrays1: Array<Array<String>>, arrayOfArrays2: Array<Array<String>>): Array<Array<String>>
        {
            var result = arrayOf<Array<String>>()

            for (array1 in arrayOfArrays1)
            {
                for (array2 in arrayOfArrays2)
                {
                    var newArray = arrayOf<String>()

                    for (array1Index in 0 until array1.size - 1)
                        newArray += array1[array1Index]

                    for (array2Index in 0 until array2.size - 1)
                        newArray += array2[array2Index]

                    newArray += array1.last() + " + " + array2.last()

                    result += newArray
                }
            }

            return result
        }

        private fun join(arrayOfArrays1: Array<Array<String>>, array2: Array<String>): Array<Array<String>>
        {
            var result = arrayOf<Array<String>>()

            for (array1 in arrayOfArrays1)
            {
                for (array2Item in array2)
                {
                    var newArray = arrayOf<String>()

                    for (array1Index in 0 until array1.size - 1)
                        newArray += array1[array1Index]

                    newArray += array2Item

                    newArray += array1.last() + " + " + array2Item

                    result += newArray
                }
            }

            return result
        }
    }
}
