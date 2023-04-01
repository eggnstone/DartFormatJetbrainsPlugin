package integration.codeBased

import TestParamBool2
import TestParams
import integration.IntegrationTools
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class BlockTestsParametrizedWithStatementOrBlockBooleans2(private val params: TestParamBool2)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = TestParams.statementOrBlockBooleans2
    }

    @Test
    fun ifElse()
    {
        val inputText =
            "if (a)\n" +
            (if (params.b0) "{}" else "a();") + "\n" +
            "else\n" +
            (if (params.b1) "{}" else "b();")

        val expectedOutputText =
            "if (a)\n" +
            (if (params.b0) "{}" else "    a();") + "\n" +
            "else\n" +
            (if (params.b1) "{}" else "    b();")

        IntegrationTools.test(inputText, expectedOutputText, true)
    }
}
