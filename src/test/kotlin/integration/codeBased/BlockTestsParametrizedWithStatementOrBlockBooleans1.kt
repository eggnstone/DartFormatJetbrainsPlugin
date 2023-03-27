package integration.codeBased

import TestParamBool1
import TestParams
import integration.IntegrationTools
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class BlockTestsParametrizedWithStatementOrBlockBooleans1(private val params: TestParamBool1)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = TestParams.statementOrBlockBooleans1
    }

    @Test
    fun simpleIf()
    {
        val inputText =
            "if (a)\n" +
                (if (params.b0) "{}" else "a();")

        val expectedOutputText =
            "if (a)\n" +
                (if (params.b0) "{}" else "    a();")

        IntegrationTools.test(inputText, expectedOutputText, true)
    }
}
