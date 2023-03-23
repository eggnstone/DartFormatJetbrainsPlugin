package integration.codeBased

import integration.IntegrationTools
import org.junit.Test

class IndentationTests
{
    @Test
    fun multipleBracketsOnlyIndentOnce()
    {
        val inputText =
            "abc({\n" +
                "def;\n" +
                "});"

        val expectedOutputText =
            "abc({\n" +
                "    def;\n" +
                "});"

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun keywordIndentsExpression()
    {
        val inputText =
            "if()\n" +
                "abc;"

        val expectedOutputText =
            "if()\n" +
                "    abc;"

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun keywordIndentsExpressionWithRoundBrackets()
    {
        val inputText =
            "if()\n" +
                "abc();"

        val expectedOutputText =
            "if()\n" +
                "    abc();"

        IntegrationTools.test(inputText, expectedOutputText)
    }

    // TODO: write non-integration test
    @Test
    fun ifBlockInsideFunction()
    {
        val inputText =
            "void main()\n" +
                "{\n" +
                "if (true)\n" +
                "{\n" +
                "abc();\n" +
                "}\n" +
                "}\n"

        val expectedText =
            "void main()\n" +
                "{\n" +
                "    if (true)\n" +
                "    {\n" +
                "        abc();\n" +
                "    }\n" +
                "}\n"

        IntegrationTools.test(inputText, expectedText)
    }
}
