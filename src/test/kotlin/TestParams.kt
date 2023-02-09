import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken

class TestParams
{
    companion object
    {
        val brackets = arrayOf(
                arrayOf("{", "}", "Angle brackets"),
                arrayOf("(", ")", "Round brackets"),
                arrayOf("<", ">", "Pointy brackets"),
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

        val lineBreaksAndKeywords = join(lineBreaks, keywords)

        val quotes = arrayOf(
                arrayOf("\"", "'", "Normal quotes"),
                arrayOf("'", "\"", "Apostrophe")
        )

        val specials = arrayOf(
                Tools.OPENING_ANGLE_BRACKET, Tools.CLOSING_ANGLE_BRACKET,
                Tools.OPENING_POINTY_BRACKET, Tools.CLOSING_POINTY_BRACKET,
                Tools.OPENING_ROUND_BRACKET, Tools.CLOSING_ROUND_BRACKET,
                Tools.OPENING_SQUARE_BRACKET, Tools.CLOSING_SQUARE_BRACKET,
                Tools.ARROW,
                Tools.COLON,
                Tools.COMMA,
                Tools.PERIOD,
                Tools.SEMICOLON
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
