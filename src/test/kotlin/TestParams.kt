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

        val statementOrBlockBooleans1 = arrayOf(
            TestParamBool1(false, "s();"),
            TestParamBool1(true, "{}")
        )

        val statementOrBlockBooleans2 = join(statementOrBlockBooleans1, statementOrBlockBooleans1)

        val statementOrBlockBooleans3 = join(statementOrBlockBooleans2, statementOrBlockBooleans1)

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

        private fun join(array1: Array<TestParamBool1>, array2: Array<TestParamBool1>): Array<TestParamBool2>
        {
            var result = arrayOf<TestParamBool2>()

            for (array1Item in array1)
                for (array2Item in array2)
                    result += TestParamBool2(array1Item.b0, array2Item.b0, array1Item.name + " + " + array2Item.name)

            return result
        }

        private fun join(array1: Array<TestParamBool2>, array2: Array<TestParamBool1>): Array<TestParamBool3>
        {
            var result = arrayOf<TestParamBool3>()

            for (array1Item in array1)
                for (array2Item in array2)
                    result += TestParamBool3(array1Item.b0, array1Item.b1, array2Item.b0, array1Item.name + " + " + array2Item.name)

            return result
        }
    }
}
