package integration.codeBased

import integration.IntegrationTools
import org.junit.Test

class EndOfLineCommentTests
{
    @Test
    fun endOfLineCommentInStatement()
    {
        val inputText =
            "if (true) //this is an end of line comment\n" +
                "abc();"

        val expectedOutputText =
            "if (true) //this is an end of line comment\n" +
                "    abc();"

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun endOfLineCommentInStatementWithMisleadingStatement()
    {
        val inputText =
            "if (true) //this is an end of line comment xyz();\n" +
                "abc();"

        val expectedOutputText =
            "if (true) //this is an end of line comment xyz();\n" +
                "    abc();"

        IntegrationTools.test(inputText, expectedOutputText)
    }
}
