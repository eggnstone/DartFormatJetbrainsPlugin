package integration.codeBased

import TestParamBool3
import TestParams
import integration.IntegrationTools
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class BlockTestsParametrizedWithStatementOrBlockBooleans3(private val params: TestParamBool3)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = TestParams.statementOrBlockBooleans3
    }

    @Test
    fun ifElseIfElse()
    {
        val inputText =
            "if (a)\n" +
                (if (params.b0) "{ab();}" else "as();") + "\n" +
                "else if (b)\n" +
                (if (params.b1) "{bb();}" else "bs();") + "\n" +
                "else\n" +
                (if (params.b2) "{cb();}" else "cs();")

        val expectedOutputText =
            "if (a)\n" +
                (if (params.b0) "{ab();}" else "    as();") + "\n" +
                "else if (b)\n" +
                (if (params.b1) "{bb();}" else "    bs();") + "\n" +
                "else\n" +
                (if (params.b2) "{cb();}" else "    cs();")

        IntegrationTools.test(inputText, expectedOutputText, true)
    }
}
