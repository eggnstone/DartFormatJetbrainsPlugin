package integration.codeBased

import integration.IntegrationTools
import org.junit.Test

class MultiLineCommentTests
{
    @Test
    fun multiLineCommentInStatement()
    {
        val inputText =
            "if (true) /*multi line comment*/\n" +
            "abc();"

        val expectedOutputText =
            "if (true) /*multi line comment*/\n" +
            "    abc();"

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun multiLineCommentInStatementWithMisleadingStatement()
    {
        val inputText =
            "if (true) /*multi line def(); comment*/\n" +
            "abc();"

        val expectedOutputText =
            "if (true) /*multi line def(); comment*/\n" +
            "    abc();"

        IntegrationTools.test(inputText, expectedOutputText)
    }
}
