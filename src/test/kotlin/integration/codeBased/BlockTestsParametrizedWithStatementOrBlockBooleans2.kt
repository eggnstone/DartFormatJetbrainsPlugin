package integration.codeBased

import TestParams
import integration.IntegrationTools
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class BlockTestsParametrizedWithStatementOrBlockBooleans2(
    useBlockText0: String,
    useBlockText1: String,
    @Suppress("UNUSED_PARAMETER") unused: String
)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{2}")
        fun data() = TestParams.statementOrBlockBooleans2
    }

    private val useBlock0: Boolean = useBlockText0 == "true"
    private val useBlock1: Boolean = useBlockText1 == "true"

    @Test
    fun ifElseIfElse()
    {
        val inputText =
            "if (a)\n" +
                (if (useBlock0) "{}" else "a();") + "\n" +
                "else\n" +
                (if (useBlock1) "{}" else "b();")

        val expectedOutputText =
            "if (a)\n" +
                (if (useBlock0) "{}" else "    a();") + "\n" +
                "else\n" +
                (if (useBlock1) "{}" else "    b();")

        IntegrationTools.test(inputText, expectedOutputText, true)
    }
}
