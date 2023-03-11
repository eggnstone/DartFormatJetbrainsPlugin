import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld
import dev.eggnstone.plugins.jetbrains.dartformat.Constants

class TestParams
{
    companion object
    {
        val classKeywords = ToolsOld.classKeywords

        val brackets = arrayOf(
            arrayOf("{", "}", "Curly brackets"),
            arrayOf("(", ")", "Round brackets"),
            arrayOf("[", "]", "Square brackets")
        )

        val brackets2 = join(brackets, brackets)

        val keywords = ToolsOld.keywords

        val lineBreaks = arrayOf(
            arrayOf("\n", "\\n"),
            arrayOf("\n\r", "\\n\\r"),
            arrayOf("\r", "\\r"),
            arrayOf("\r\n", "\\r\\n")
        )

        val lineBreaksAndBrackets = join(lineBreaks, brackets)

        val lineBreaksAndKeywords = join(lineBreaks, keywords)

        val quotes = arrayOf(
            arrayOf("\"", "Normal quotes"),
            arrayOf("'", "Apostrophes")
        )

        val quotesWithOtherQuotes = arrayOf(
            arrayOf("\"", "'", "Normal quotes and apostrophes"),
            arrayOf("'", "\"", "Apostrophes and normal quotes")
        )

        val quotesWithInnerQuotes = arrayOf(
            arrayOf("\"", "'", "Normal quotes with inner apostrophes"),
            arrayOf("\"", "\\\"", "Normal quotes with inner escaped normal quotes"),
            arrayOf("'", "\"", "Apostrophes with inner normal quotes"),
            arrayOf("'", "\\'", "Apostrophes with inner escaped apostrophes")
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

        val whitespaces = arrayOf(
            arrayOf(" ", "Space"),
            arrayOf("\t", "Tab"),
            arrayOf("\n", "\\n"),
            arrayOf("\n\r", "\\n\\r"),
            arrayOf("\r", "\\r"),
            arrayOf("\r\n", "\\r\\n")
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

        private fun join(array1: Array<Array<String>>, array2: Array<String>): Array<Array<String>>
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
